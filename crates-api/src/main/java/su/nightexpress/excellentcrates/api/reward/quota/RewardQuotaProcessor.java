package su.nightexpress.excellentcrates.api.reward.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardQuotaProcessor {

    ActionResult test(Player player, Crate crate, Reward reward);

    QuotaThreshold getThreshold(Player player, Crate crate, Reward reward);

    void apply(Player player, Crate crate, Reward reward);
}
