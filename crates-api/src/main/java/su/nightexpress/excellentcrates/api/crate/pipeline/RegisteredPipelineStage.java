package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record RegisteredPipelineStage(PipelinePhase phase,
                                      int order,
                                      PipelineStage stage) {
}