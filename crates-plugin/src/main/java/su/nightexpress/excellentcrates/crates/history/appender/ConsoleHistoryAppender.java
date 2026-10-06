package su.nightexpress.excellentcrates.crates.history.appender;

import java.util.stream.Collectors;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.api.crate.history.CrateHistoryAppender;
import su.nightexpress.excellentcrates.api.crate.history.HistoryLog;

@NullMarked
public class ConsoleHistoryAppender implements CrateHistoryAppender {

    private static final Logger LOGGER = LoggerFactory.getLogger("CrateHistory");

    @Override
    public void append(HistoryLog log) {
        String logEntry = log.data().entrySet().stream()
            .map(entry -> entry.getKey() + ": " + entry.getValue())
            .collect(Collectors.joining(" | "));

        LOGGER.info(logEntry);
    }
}
