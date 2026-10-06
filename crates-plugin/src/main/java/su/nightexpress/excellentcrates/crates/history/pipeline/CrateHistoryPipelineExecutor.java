package su.nightexpress.excellentcrates.crates.history.pipeline;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.history.CrateHistoryAppender;
import su.nightexpress.excellentcrates.api.crate.history.HistoryLog;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.LoggablePipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;

@NullMarked
public class CrateHistoryPipelineExecutor implements PipelineExecutor {

    private final CrateHistoryAppender appender;

    public CrateHistoryPipelineExecutor(CrateHistoryAppender appender) {
        this.appender = appender;
    }

    @Override
    public void execute(Player player, Crate crate, PipelineContext context) {
        LocalDateTime timestamp = LocalDateTime.now();

        Map<String, String> logData = new LinkedHashMap<>();

        logData.put("Player", player.getName());
        logData.put("Crate", crate.idString());

        // Iterate over all components in the context to collect loggable data
        for (PipelineComponent component : context.getComponents().values()) {

            // If the component can be logged, collect its data
            if (component instanceof LoggablePipelineComponent loggable) {
                Map<String, String> componentLogData = loggable.getLogData();

                if (componentLogData != null && !componentLogData.isEmpty()) {
                    logData.putAll(componentLogData);
                }
            }
        }

        HistoryLog log = new HistoryLog(timestamp, logData);

        // Log the collected information
        this.appender.append(log);
    }
}
