package su.nightexpress.excellentcrates.reward.feature.limit.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.api.common.limit.LimitRemaining;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.limit.RewardLimitManageService;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class RewardLimitsPlaceholder implements RewardPlaceholder {

    private final RewardLimitManageService limitService;

    public RewardLimitsPlaceholder(RewardLimitManageService limitService) {
        this.limitService = limitService;
    }

    @Override
    public Consumer<Builder> applyInCrate(CrateRewardEntry crateReward, Crate crate, Reward reward,
                                          @Nullable Player player) {
        return ctx -> {
            if (player != null) {
                this.addRemainingPlaceholder(ctx, reward, player);
            }
        };
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return ctx -> {
            this.addCapacityWithMarkerPlaceholder(ctx, reward);
        };
    }

    private void addRemainingPlaceholder(Builder ctx, Reward reward, Player player) {
        ctx.with(SharedPlaceholders.REWARD_LIMIT_REMAINING, () -> {
            LimitRemaining remaining = limitService.getRemainingRolls(player, reward);
            if (remaining.isUnlimited()) return CoreLang.OTHER_INFINITY.text();

            return NumberUtil.format(remaining.getRemaining());
        });
    }

    private void addCapacityWithMarkerPlaceholder(Builder ctx, Reward reward) {
        RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        if (limit == null) return;

        LimitOptions strictest = null;
        for (LimitType type : LimitType.values()) {
            LimitOptions options = limit.getOptions(type);
            if (!options.isEnabled()) continue;
            if (strictest == null || options.getAmount() < strictest.getAmount()) {
                strictest = options;
            }
        }

        LimitOptions finalStrictest = strictest;

        ctx.with(SharedPlaceholders.REWARD_LIMIT_CAPACITY, () -> {
            if (finalStrictest == null) return CoreLang.OTHER_INFINITY.text();

            return NumberUtil.format(finalStrictest.getAmount());
        });

        if (finalStrictest != null) {
            ctx.with(SharedPlaceholders.REWARD_HAS_LIMIT_MARKER, () -> String.valueOf(true));
        }
    }
}
