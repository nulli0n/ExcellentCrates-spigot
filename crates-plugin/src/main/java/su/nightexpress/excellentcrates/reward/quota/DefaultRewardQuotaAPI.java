package su.nightexpress.excellentcrates.reward.quota;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.quota.RegisteredQuotaProcessor;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaAPI;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaProcessor;

@NullMarked
public class DefaultRewardQuotaAPI implements RewardQuotaAPI {

    private final TinyRegistry<RegisteredQuotaProcessor> processors;
    private final RewardQuotaService                     quotaService;

    public DefaultRewardQuotaAPI(TinyRegistry<RegisteredQuotaProcessor> processors, RewardQuotaService quotaService) {
        this.processors = processors;
        this.quotaService = quotaService;
    }

    @Override
    public void applyQuotas(Player player, Crate crate, Reward reward) {
        this.quotaService.applyQuotas(player, crate, reward);
    }

    @Override
    public QuotaThreshold getLeastThreshold(Player player, Crate crate, Reward reward) {
        return this.quotaService.getLeastThreshold(player, crate, reward);
    }

    @Override
    public TinyRegistry<RegisteredQuotaProcessor> getProcessors() {
        return this.processors;
    }

    @Override
    public void registerProcessor(int priority, RewardQuotaProcessor processor) {
        this.processors.register(new RegisteredQuotaProcessor(priority, processor));
    }

    @Override
    public ActionResult testQuotas(Player player, Crate crate, Reward reward) {
        return this.quotaService.testQuotas(player, crate, reward);
    }
}
