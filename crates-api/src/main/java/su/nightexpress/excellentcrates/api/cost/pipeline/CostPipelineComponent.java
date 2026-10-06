package su.nightexpress.excellentcrates.api.cost.pipeline;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineComponent;

@NullMarked
public interface CostPipelineComponent extends PipelineComponent {

    Identifier getCostTypeId();

    String getCostOptionId();
}
