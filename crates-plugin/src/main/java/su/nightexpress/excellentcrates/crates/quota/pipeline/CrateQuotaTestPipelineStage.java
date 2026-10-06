package su.nightexpress.excellentcrates.crates.quota.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.quota.QuotaThreshold;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.crates.quota.CrateQuotaService;

@NullMarked
public class CrateQuotaTestPipelineStage implements PipelineStage {

    private final CrateQuotaService      quotaService;
    private final CrateMessageDispatcher dispatcher;

    public CrateQuotaTestPipelineStage(CrateQuotaService quotaService, CrateMessageDispatcher dispatcher) {
        this.quotaService = quotaService;
        this.dispatcher = dispatcher;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        ActionResult result = this.quotaService.testQuotas(player, crate);
        if (result.failure()) {
            result.handleFeedback((locale, ctx) -> this.dispatcher.sendBase(player, crate, locale, ctx));
            chain.abort();
            return;
        }

        context.getComponent(PipelineComponentKeys.BATCH).ifPresent(batchComponent -> {
            QuotaThreshold leastThreshold = this.quotaService.getLeastThreshold(player, crate);
            if (!leastThreshold.isAbsent()) {
                int limit = leastThreshold.threshold();
                batchComponent.limitMaxAllowed(limit);
            }
        });

        chain.proceed(player, context);
    }
}
