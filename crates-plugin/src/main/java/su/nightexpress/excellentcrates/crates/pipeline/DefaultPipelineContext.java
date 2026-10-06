package su.nightexpress.excellentcrates.crates.pipeline;

import java.util.HashMap;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.MutableEntityComponentContainer;
import su.nightexpress.engine.entity.MutableEntityComponents;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class DefaultPipelineContext implements PipelineContext {

    private final Crate                                      crate;
    private final MutableEntityComponents<PipelineComponent> components;

    public DefaultPipelineContext(Crate crate) {
        this.crate = crate;
        this.components = new MutableEntityComponentContainer<>(new HashMap<>());
    }

    @Override
    public Crate getCrate() {
        return this.crate;
    }

    @Override
    public MutableEntityComponents<PipelineComponent> getComponents() {
        return this.components;
    }
}
