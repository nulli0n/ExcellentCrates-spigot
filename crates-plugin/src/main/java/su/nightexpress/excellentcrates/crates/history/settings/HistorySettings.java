package su.nightexpress.excellentcrates.crates.history.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record HistorySettings(boolean logConsole, boolean logFile, String dateFormat) {

    public static HistorySettings defaultSettings() {
        return new HistorySettings(
            Schema.LOG_CONSOLE.getDefaultValue(),
            Schema.LOG_FILE.getDefaultValue(),
            Schema.DATE_FORMAT.getDefaultValue()
        );
    }

    public static HistorySettings loadFrom(Path path) {
        FileConfig config = FileConfig.load(path);

        boolean logConsole = config.getOrSet(Schema.LOG_CONSOLE);
        boolean logFile = config.getOrSet(Schema.LOG_FILE);
        String dateFormat = config.getOrSet(Schema.DATE_FORMAT);

        config.saveChanges();

        return new HistorySettings(logConsole, logFile, dateFormat);
    }

    private static final class Schema {

        static final ConfigProperty<Boolean> LOG_CONSOLE = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "log_to_console",
            true,
            "Enable or disable logging to the console."
        );

        static final ConfigProperty<Boolean> LOG_FILE = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "log_to_file",
            true,
            "Enable or disable logging to a file."
        );

        static final ConfigProperty<String> DATE_FORMAT = ConfigProperty.of(ConfigCodecs.STRING,
            "date_format",
            "yyyy-MM-dd HH:mm:ss",
            "The date format to use in the logs."
        );
    }
}
