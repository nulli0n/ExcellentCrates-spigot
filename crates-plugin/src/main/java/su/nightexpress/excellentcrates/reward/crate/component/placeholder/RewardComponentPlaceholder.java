package su.nightexpress.excellentcrates.reward.crate.component.placeholder;

import java.util.Objects;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholder;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class RewardComponentPlaceholder implements CratePlaceholder {

    private final RewardResolver rewardResolver;

    public RewardComponentPlaceholder(RewardResolver rewardResolver) {
        this.rewardResolver = rewardResolver;
    }

    @Override
    public PlaceholderApplier apply(Crate crate) {
        return ctx -> {
            ctx.with(SharedPlaceholders.CRATE_TOTAL_REWARDS_WEIGHT, () -> {
                return NumberUtil.format(this.getTotalWeight(crate));
            });
        };
    }

    @Override
    public PlaceholderApplier apply(Crate crate, Player player) {
        return PlaceholderApplier.empty();
    }

    private double getTotalWeight(Crate crate) {
        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) return 0D;

        return crateRewards.getRewards().stream()
            .map(entry -> this.rewardResolver.resolveReward(crate.id(), entry.getRewardId()))
            .filter(Objects::nonNull)
            .mapToDouble(reward -> reward.getBase().getWeight())
            .sum();
    }
}
