package su.nightexpress.excellentcrates.api.reward.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardQuotaAPI {

    void registerProcessor(int priority, RewardQuotaProcessor processor);

    TinyRegistry<RegisteredQuotaProcessor> getProcessors();

    QuotaThreshold getLeastThreshold(Player player, Crate crate, Reward reward);

    ActionResult testQuotas(Player player, Crate crate, Reward reward);

    void applyQuotas(Player player, Crate crate, Reward reward);
}
