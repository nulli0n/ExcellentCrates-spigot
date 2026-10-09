package su.nightexpress.excellentcrates.reward.quota.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class RewardQuotaPlaceholder implements RewardPlaceholder {

    private final RewardQuotaService quotaService;

    public RewardQuotaPlaceholder(RewardQuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return ctx -> {

        };
    }

    @Override
    public Consumer<Builder> applyInCrate(Crate crate, Reward reward, @Nullable Player player) {
        return ctx -> {
            if (player != null) {
                ctx.with(SharedPlaceholders.REWARD_QUOTA_THRESHOLD, () -> {
                    QuotaThreshold threshold = this.quotaService.getLeastThreshold(player, crate, reward);
                    if (threshold.isAbsent()) return CoreLang.OTHER_INFINITY.text();

                    return NumberUtil.format(threshold.getThreshold());
                });
            }
        };
    }

}
