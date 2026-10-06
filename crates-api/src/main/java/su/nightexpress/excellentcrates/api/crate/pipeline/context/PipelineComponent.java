package su.nightexpress.excellentcrates.api.crate.pipeline.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponent;

/**
 * A component that can be attached to a {@link PipelineContext} to provide additional data or behavior.
 */
@NullMarked
public interface PipelineComponent extends EntityComponent {

}
