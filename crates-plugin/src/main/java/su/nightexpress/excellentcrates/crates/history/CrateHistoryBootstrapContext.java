package su.nightexpress.excellentcrates.crates.history;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.history.CrateHistoryAppender;
import su.nightexpress.excellentcrates.crates.history.appender.AsyncFileHistoryAppender;
import su.nightexpress.excellentcrates.crates.history.appender.CompositeHistoryAppender;
import su.nightexpress.excellentcrates.crates.history.appender.ConsoleHistoryAppender;
import su.nightexpress.excellentcrates.crates.history.controller.AsyncFileHistoryCloseController;
import su.nightexpress.excellentcrates.crates.history.pipeline.CrateHistoryPipelineExecutor;
import su.nightexpress.excellentcrates.crates.history.settings.HistorySettings;

@NullMarked
public class CrateHistoryBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.history");
    private static final String     NAME = "History";

    private static final String SETTINGS_FILE_NAME = "crates.history.yml";
    private static final String LOG_FILE_NAME      = "crate_history.log";

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateHistoryBootstrapContext.class);

    private final CrateHistoryPipelineExecutor pipelineExecutor;

    public CrateHistoryBootstrapContext(Path configPath, Path logsPath) {
        super(ID, NAME);

        Path settingsFile = configPath.resolve(SETTINGS_FILE_NAME);
        HistorySettings settings = HistorySettings.loadFrom(settingsFile);

        List<CrateHistoryAppender> appenders = new ArrayList<>();

        if (settings.logFile()) {
            this.initFileAppender(logsPath, settings, appenders::add);
        }

        if (settings.logConsole()) {
            ConsoleHistoryAppender consoleAppender = new ConsoleHistoryAppender();
            appenders.add(consoleAppender);
        }

        if (appenders.isEmpty()) {
            LOGGER.warn(
                "No history appenders configured, crate history will not be logged. It's recommended to disable crate history if logging is not needed."
            );
        }

        CrateHistoryAppender appender = new CompositeHistoryAppender(appenders);
        this.pipelineExecutor = new CrateHistoryPipelineExecutor(appender);
    }

    private void initFileAppender(Path logsPath, HistorySettings settings, Consumer<CrateHistoryAppender> callback) {
        Path logFilePath = logsPath.resolve(LOG_FILE_NAME);
        if (!Files.exists(logFilePath)) {
            try {
                Files.createDirectories(logsPath);
                Files.createFile(logFilePath);
            }
            catch (IOException e) {
                LOGGER.error("Failed to create log file: {}", logFilePath, e);
            }
        }

        DateTimeFormatter dateTimeFormatter;
        try {
            dateTimeFormatter = DateTimeFormatter.ofPattern(settings.dateFormat());
        }
        catch (IllegalArgumentException e) {
            LOGGER.error("Failed to parse date format: {}", settings.dateFormat(), e);
            return;
        }

        AsyncFileHistoryAppender appender = new AsyncFileHistoryAppender(logFilePath, dateTimeFormatter);
        callback.accept(appender);

        this.addComponent(new AsyncFileHistoryCloseController(appender));
    }

    public CrateHistoryPipelineExecutor getPipelineExecutor() {
        return this.pipelineExecutor;
    }
}
