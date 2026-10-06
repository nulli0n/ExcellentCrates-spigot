package su.nightexpress.excellentcrates.reward.feature.cooldown.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;
import su.nightexpress.excellentcrates.reward.feature.cooldown.RewardCooldownService;

@NullMarked
public class RewardCooldownsQuotaProcessor implements RewardQuotaProcessor {

    private final RewardCooldownService cooldownService;

    public RewardCooldownsQuotaProcessor(RewardCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public ActionResult test(Player player, Crate crate, Reward reward) {
        CooldownTimestamp timestamp = this.cooldownService.getExpirationTimestamp(player, reward);
        if (timestamp == null || timestamp.isExpired()) {
            return ActionResult.ok();
        }

        return ActionResult.fail();
    }

    @Override
    public QuotaThreshold getThreshold(Player player, Crate crate, Reward reward) {
        if (!this.test(player, crate, reward).success()) {
            return QuotaThreshold.of(0);
        }

        if (this.cooldownService.hasCooldownConfigured(player, reward)) {
            return QuotaThreshold.of(1);
        }

        return QuotaThreshold.absent();
    }

    @Override
    public void apply(Player player, Crate crate, Reward reward) {
        this.cooldownService.applyCooldowns(player, reward);
    }
}
