package su.nightexpress.excellentcrates.crates.quota.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineChain;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineStage;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.crates.quota.CrateQuotaService;

@NullMarked
public class CrateQuotaApplyPipelineStage implements PipelineStage {

    private final CrateQuotaService quotaService;

    public CrateQuotaApplyPipelineStage(CrateQuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @Override
    public void intercept(Player player, Crate crate, PipelineContext context, PipelineChain chain) {
        this.quotaService.applyQuotas(player, crate);
        chain.proceed(player, context);
    }
}
