package su.nightexpress.excellentcrates.api.crate.pipeline.component;

import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineComponent;

@NullMarked
public interface LoggablePipelineComponent extends PipelineComponent {

    /**
     * Retrieves the log data associated with this pipeline context component.
     *
     * @return a map containing the log data, where the keys and values are both strings or empty map if there is
     *         nothing to log.
     */
    Map<String, String> getLogData();
}
