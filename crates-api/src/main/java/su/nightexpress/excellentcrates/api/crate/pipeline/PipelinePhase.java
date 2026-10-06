package su.nightexpress.excellentcrates.api.crate.pipeline;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum PipelinePhase {

    BUILD,
    PRE_ROLL,
    ROLL,
    MODIFICATION,
    VALIDATION,
    PRE_PROCESS,
    ;
}
