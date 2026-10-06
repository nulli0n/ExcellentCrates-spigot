package su.nightexpress.excellentcrates.api.crate.batch;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.component.LoggablePipelineComponent;

@NullMarked
public interface BatchPipelineComponent extends LoggablePipelineComponent {

    int getMaxAllowed();

    void limitMaxAllowed(int reduced);

    int getSelectedAmount();

    void setSelectedAmount(int amount);
}
