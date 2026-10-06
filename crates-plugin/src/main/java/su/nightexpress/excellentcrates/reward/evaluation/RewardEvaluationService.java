package su.nightexpress.excellentcrates.reward.evaluation;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.evaluation.RewardEvaluator;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;
import su.nightexpress.excellentcrates.util.WeightedRandomSelector;
import su.nightexpress.nightcore.util.Randomizer;

@NullMarked
public class RewardEvaluationService implements RewardEvaluator {

    private static final int NEGATIVE_WEIGHT = 0;

    private final RewardResolver     rewardResolver;
    private final RewardQuotaService quotaService;

    public RewardEvaluationService(RewardResolver rewardResolver,
                                   RewardQuotaService quotaService) {
        this.rewardResolver = rewardResolver;
        this.quotaService = quotaService;
    }

    @Override
    public @Nullable CrateRewardsComponent getRewardsComponent(Crate crate) {
        return crate.getComponentOrNull(CrateComponentKeys.REWARDS);
    }

    @Override
    public List<Reward> getAvailableRewards(Crate crate, Player player) {
        return this.resolveAvailableRewards(crate, player).stream().map(RollableReward::reward).toList();
    }

    @Override
    public int getRequiredRewards(Crate crate) {
        CrateRewardsComponent rewards = this.getRewardsComponent(crate);
        return rewards == null ? 0 : rewards.getRequiredAmount();
    }

    public Set<RollableReward> resolveAvailableRewards(Crate crate, Player player) {
        CrateRewardsComponent rewards = this.getRewardsComponent(crate);
        if (rewards == null) return Set.of();

        Set<RollableReward> resolvedRewards = new HashSet<>();

        for (CrateRewardEntry rewardEntry : rewards.getRewards()) {
            Reward reward = this.rewardResolver.resolveReward(rewardEntry.getRewardId());
            if (reward == null || rewardEntry.getWeight() <= NEGATIVE_WEIGHT) continue;

            QuotaThreshold threshold = this.quotaService.getLeastThreshold(player, crate, reward);
            if (threshold.isExhausted()) continue;

            resolvedRewards.add(new RollableReward(reward, rewardEntry.getWeight(), threshold));
        }

        return resolvedRewards;
    }

    @Override
    public boolean isRewardAvailable(Crate crate, Reward reward, Player player) {
        return player == null || this.quotaService.testQuotas(player, crate, reward).success();
    }

    @Override
    public Optional<Reward> rollReward(Crate crate, Player player) {
        Set<RollableReward> resolvedRewards = this.resolveAvailableRewards(crate, player);
        if (resolvedRewards.isEmpty()) return Optional.empty();

        WeightedRandomSelector selector = new WeightedRandomSelector(Randomizer.getSource());
        RollableReward target = selector.selectItem(resolvedRewards, RollableReward::weight);

        return Optional.of(target.reward());
    }

    @Override
    public Optional<Reward> rollReward(Crate crate, Rarity rarity, Player player) {
        WeightedRandomSelector selector = new WeightedRandomSelector(Randomizer.getSource());
        Set<RollableReward> targetRewards = new HashSet<>();

        for (RollableReward weightedReward : this.resolveAvailableRewards(crate, player)) {
            Reward reward = weightedReward.reward();
            RarityComponent rarityComponent = reward.getComponentOrNull(RewardComponentKeys.RARITY);
            if (rarityComponent == null) continue;
            if (!rarityComponent.isEnabled()) continue;
            if (!rarityComponent.getRarityId().equals(rarity.id())) continue;

            targetRewards.add(weightedReward);
        }

        if (targetRewards.isEmpty()) return Optional.empty();

        RollableReward target = selector.selectItem(targetRewards, RollableReward::weight);

        return Optional.of(target.reward());
    }

    @Override
    public double calculateRollChance(Crate crate, Reward reward) {
        return this.calculateProbability(crate, reward) * 100D;
    }

    @Override
    public double calculateProbability(Crate crate, Reward reward) {
        return this.calculateRewardProbability(crate, reward.getId());
    }

    @Override
    public double calculateRewardProbability(Crate crate, Identifier rewardId) {
        CrateRewardsComponent rewards = this.getRewardsComponent(crate);
        if (rewards == null) return 0D;

        CrateRewardEntry rewardEntry = rewards.getReward(rewardId);
        if (rewardEntry == null || rewardEntry.getWeight() <= NEGATIVE_WEIGHT) return 0D;

        double sum = 0D;

        for (CrateRewardEntry competitor : rewards.getRewards()) {
            if (competitor.getWeight() <= NEGATIVE_WEIGHT) continue;

            sum += competitor.getWeight();
        }

        if (sum <= NEGATIVE_WEIGHT) return 0D;

        return rewardEntry.getWeight() / sum;
    }
}
