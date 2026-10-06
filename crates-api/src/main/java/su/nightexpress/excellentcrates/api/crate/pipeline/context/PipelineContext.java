package su.nightexpress.excellentcrates.api.crate.pipeline.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.MutableComponentEntity;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface PipelineContext extends MutableComponentEntity<PipelineComponent> {

    Crate getCrate();
}
