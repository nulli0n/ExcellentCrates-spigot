package su.nightexpress.excellentcrates.api.crate.pipeline.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.cost.pipeline.CostPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.batch.BatchPipelineComponent;

/**
 * Contains the keys for accessing common pipeline components.
 */
@NullMarked
public final class PipelineComponentKeys {

    public static final EntityComponentKey<CrateSourcePipelineComponent> CRATE_SOURCE = EntityComponentKey.of(
        "crate_source",
        CrateSourcePipelineComponent.class
    );

    public static final EntityComponentKey<CrateRewardsPipelineComponent> REWARDS = EntityComponentKey.of(
        "rewards",
        CrateRewardsPipelineComponent.class
    );

    public static final EntityComponentKey<AnimationProfileComponent> ANIMATION = EntityComponentKey.of(
        "animation",
        AnimationProfileComponent.class
    );

    public static final EntityComponentKey<BatchPipelineComponent> BATCH = EntityComponentKey.of(
        "batch",
        BatchPipelineComponent.class
    );

    public static final EntityComponentKey<FastOpenPipelineComponent> FAST_OPEN = EntityComponentKey.of(
        "fast_open",
        FastOpenPipelineComponent.class
    );

    public static final EntityComponentKey<FreeOpenPipelineComponent> FREE_OPEN = EntityComponentKey.of(
        "free_open",
        FreeOpenPipelineComponent.class
    );

    public static final EntityComponentKey<CostPipelineComponent> COST = EntityComponentKey.of(
        "selected_cost",
        CostPipelineComponent.class
    );

    private PipelineComponentKeys() {
    }
}
