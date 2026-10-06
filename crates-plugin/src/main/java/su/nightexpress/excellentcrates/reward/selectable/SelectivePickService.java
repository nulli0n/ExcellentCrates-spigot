package su.nightexpress.excellentcrates.reward.selectable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;

@NullMarked
public class SelectivePickService {

    private final RewardRegistry     rewardRegistry;
    private final RewardQuotaService rewardQuotaService;

    public SelectivePickService(RewardRegistry rewardRegistry,
                                RewardQuotaService rewardQuotaService) {
        this.rewardRegistry = rewardRegistry;
        this.rewardQuotaService = rewardQuotaService;
    }

    public List<Reward> getAvailableRewards(Player player, Crate crate, SelectivePickContext pickContext) {
        CrateRewardsComponent rewardsComponent = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (rewardsComponent == null) {
            return List.of();
        }

        return rewardsComponent.getRewards().stream()
            .map(entry -> this.rewardRegistry.get(entry.getRewardId()))
            .filter(Objects::nonNull)
            .filter(reward -> this.isPickable(player, crate, reward, pickContext).success())
            .sorted(Comparator.comparing(Reward::idString))
            .toList();
    }

    public ActionResult validatePick(Player player, Crate crate, SelectivePickContext pickContext) {
        int requiredRewards = pickContext.requiredRewards();
        List<RewardReference> selectedRewards = pickContext.selectedRewards();

        // Use empty context to test whether the selected rewards are still pickable without considering the current selection
        SelectivePickContext emptyContext = new SelectivePickContext(requiredRewards, List.of());

        selectedRewards.removeIf(selectedRef -> {
            Reward reward = selectedRef.get();
            return reward == null || this.isPickable(player, crate, reward, emptyContext).failure();
        });

        int availableRewards = this.getAvailableRewards(player, crate, pickContext).size();
        int selectedAmount = selectedRewards.size();

        // Only disallow if there are still available rewards to pick from
        if (selectedAmount < requiredRewards && availableRewards > 0) {
            return ActionResult.fail();
        }

        return ActionResult.ok();
    }

    public ActionResult isPickable(Player player, Crate crate, Reward reward, SelectivePickContext pickContext) {
        Identifier rewardId = reward.id();

        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) {
            return ActionResult.fail();
        }

        if (!crateRewards.hasReward(rewardId)) {
            return ActionResult.fail();
        }

        // Clean up selected rewards that are no longer valid
        List<RewardReference> selectedRewards = pickContext.selectedRewards();
        if (!selectedRewards.isEmpty()) {
            selectedRewards.removeIf(selectedRef -> {
                return !crateRewards.hasReward(selectedRef.id());
            });
        }

        if (selectedRewards.size() >= pickContext.requiredRewards()) {
            return ActionResult.fail();
        }

        QuotaThreshold quotaThreshold = this.rewardQuotaService.getLeastThreshold(player, crate, reward);
        if (quotaThreshold.isExhausted()) {
            return ActionResult.fail();
        }

        if (!quotaThreshold.isAbsent()) {
            long countSelected = pickContext.selectedRewards().stream()
                .filter(selectedRef -> selectedRef.id().equals(reward.id()))
                .count();

            if (countSelected >= quotaThreshold.getThreshold()) {
                return ActionResult.fail();
            }
        }

        return ActionResult.ok();
    }

    public ActionResult pickReward(Player player, Crate crate, Reward reward, SelectivePickContext pickContext) {
        ActionResult result = isPickable(player, crate, reward, pickContext);
        if (!result.success()) {
            return result;
        }

        Identifier rewardId = reward.id();
        List<RewardReference> selectedRewards = pickContext.selectedRewards();

        selectedRewards.add(this.rewardRegistry.createReference(rewardId));

        return ActionResult.ok();
    }

    public ActionResult unpickReward(Player player, Crate crate, Reward reward, SelectivePickContext pickContext) {
        Identifier rewardId = reward.id();
        List<RewardReference> selectedRewards = pickContext.selectedRewards();

        RewardReference existingRef = selectedRewards.stream()
            .filter(ref -> ref.id().equals(rewardId))
            .findFirst()
            .orElse(null);

        if (existingRef != null) {
            selectedRewards.remove(existingRef);
            return ActionResult.ok();
        }

        return ActionResult.fail();
    }
}
