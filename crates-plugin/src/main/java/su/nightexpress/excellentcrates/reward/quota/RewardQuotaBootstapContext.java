package su.nightexpress.excellentcrates.reward.quota;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.quota.RegisteredQuotaProcessor;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaAPI;
import su.nightexpress.excellentcrates.reward.quota.pipeline.RewardQuotaApplyExecutor;
import su.nightexpress.excellentcrates.reward.quota.placeholder.RewardQuotaPlaceholder;

@NullMarked
public class RewardQuotaBootstapContext {

    public final RewardQuotaService quotaService;
    public final RewardQuotaAPI     api;

    private final RewardQuotaApplyExecutor applyExecutor;
    private final RewardQuotaPlaceholder   placeholder;

    public RewardQuotaBootstapContext() {
        TinyRegistry<RegisteredQuotaProcessor> processors = new SimpleRegistry<>();

        this.quotaService = new RewardQuotaService(processors);
        this.api = new DefaultRewardQuotaAPI(processors, this.quotaService);

        this.applyExecutor = new RewardQuotaApplyExecutor(this.quotaService);
        this.placeholder = new RewardQuotaPlaceholder(this.quotaService);
    }

    public RewardQuotaApplyExecutor getPipelineExecutor() {
        return this.applyExecutor;
    }

    public RewardQuotaPlaceholder getPlaceholder() {
        return this.placeholder;
    }
}
