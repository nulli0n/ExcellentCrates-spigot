package su.nightexpress.excellentcrates.reward.feature.limit.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.limit.LimitRemaining;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;
import su.nightexpress.excellentcrates.reward.feature.limit.RewardLimitManageService;

@NullMarked
public class RewardLimitsQuotaProcessor implements RewardQuotaProcessor {

    private final RewardLimitManageService manageService;

    public RewardLimitsQuotaProcessor(RewardLimitManageService manageService) {
        this.manageService = manageService;
    }

    @Override
    public void apply(Player player, Crate crate, Reward reward) {
        this.manageService.incrementRolls(player, reward);
    }

    @Override
    public ActionResult test(Player player, Crate crate, Reward reward) {
        if (this.manageService.isAllowedToRoll(player, reward)) {
            return ActionResult.ok();
        }
        return ActionResult.fail();
    }

    @Override
    public QuotaThreshold getThreshold(Player player, Crate crate, Reward reward) {
        LimitRemaining remaining = this.manageService.getRemainingRolls(player, reward);
        if (remaining.isUnlimited()) return QuotaThreshold.absent();
        if (remaining.isExhausted()) return QuotaThreshold.of(0);

        return QuotaThreshold.of(remaining.getRemaining());
    }
}
