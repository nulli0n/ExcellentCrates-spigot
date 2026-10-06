package su.nightexpress.excellentcrates.crates.quota;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.quota.CrateQuotaProcessor;
import su.nightexpress.excellentcrates.crates.quota.pipeline.CrateQuotaApplyPipelineStage;
import su.nightexpress.excellentcrates.crates.quota.pipeline.CrateQuotaTestPipelineStage;

@NullMarked
public class CrateQuotaBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.quota");
    private static final String     NAME = "Quota";

    public final TinyRegistry<CrateQuotaProcessor> quotaProcessors;

    public final CrateQuotaService quotaService;

    private final CrateQuotaTestPipelineStage  testPipelineStage;
    private final CrateQuotaApplyPipelineStage applyPipelineStage;

    public CrateQuotaBootstrapContext(CrateMessageDispatcher dispatcher) {
        super(ID, NAME);

        this.quotaProcessors = new SimpleRegistry<>();
        this.quotaService = new CrateQuotaService(this.quotaProcessors);

        this.testPipelineStage = new CrateQuotaTestPipelineStage(this.quotaService, dispatcher);
        this.applyPipelineStage = new CrateQuotaApplyPipelineStage(this.quotaService);
    }

    public CrateQuotaTestPipelineStage getTestPipelineStage() {
        return this.testPipelineStage;
    }

    public CrateQuotaApplyPipelineStage getApplyPipelineStage() {
        return this.applyPipelineStage;
    }
}
