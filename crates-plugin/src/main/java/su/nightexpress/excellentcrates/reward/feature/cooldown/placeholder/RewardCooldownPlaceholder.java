package su.nightexpress.excellentcrates.reward.feature.cooldown.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.excellentcrates.reward.feature.cooldown.RewardCooldownService;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;
import su.nightexpress.nightcore.util.time.TimeFormatType;
import su.nightexpress.nightcore.util.time.TimeFormats;

@NullMarked
public class RewardCooldownPlaceholder implements RewardPlaceholder {

    private final RewardCooldownService cooldownService;

    public RewardCooldownPlaceholder(RewardCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public Consumer<Builder> applyInCrate(Crate crate, Reward reward, @Nullable Player player) {
        return ctx -> {
            if (player != null) {
                this.addEffectiveCooldownPlaceholder(ctx, reward, player);
            }
        };
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return ctx -> {
            this.addExpectedCooldownPlaceholder(ctx, reward);
        };
    }

    private void addEffectiveCooldownPlaceholder(Builder ctx, Reward reward, Player player) {
        CooldownTimestamp timestamp = this.cooldownService.getExpirationTimestamp(player, reward);

        ctx.with(SharedPlaceholders.REWARD_ACTIVE_COOLDOWN, () -> {
            if (timestamp == null || timestamp.isExpired()) return Lang.FORMAT_COOLDOWN_NONE.text();
            if (timestamp.isPermanent()) return Lang.FORMAT_COOLDOWN_PERMANENT.text();

            return TimeFormats.formatDuration(timestamp.expirationTimestamp(), TimeFormatType.LITERAL);
        });

        if (timestamp == null || timestamp.isExpired()) return;

        ctx.with(SharedPlaceholders.REWARD_HAS_ACTIVE_COOLDOWN_MARKER, () -> {
            return String.valueOf(true);
        });
    }

    private void addExpectedCooldownPlaceholder(Builder ctx, Reward reward) {
        RewardCooldownComponent cooldowns = this.cooldownService.getRewardCooldowns(reward);
        if (cooldowns == null) return;

        CooldownOptions best = null;

        for (CooldownType type : CooldownType.values()) {
            CooldownOptions cooldown = cooldowns.getCooldown(type);
            if (!cooldown.isEffectivelyEnabled()) continue;

            if (best == null || cooldown.hasBiggerDuration(best)) {
                best = cooldown;
            }
        }

        CooldownOptions finalBest = best;

        ctx.with(SharedPlaceholders.REWARD_EXPECTED_COOLDOWN, () -> {
            if (finalBest == null) return Lang.FORMAT_COOLDOWN_NONE.text();

            return TimeFormats.formatAmount(finalBest.getDurationMillis(), TimeFormatType.LITERAL);
        });

        if (finalBest != null) {
            ctx.with(SharedPlaceholders.REWARD_HAS_EXPECTED_COOLDOWN_MARKER, () -> {
                return String.valueOf(true);
            });
        }
    }
}
