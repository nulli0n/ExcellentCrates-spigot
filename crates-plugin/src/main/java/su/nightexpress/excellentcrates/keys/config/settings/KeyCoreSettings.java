package su.nightexpress.excellentcrates.keys.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record KeyCoreSettings(String[] commandAliases,
                              int dataSaveInterval,
                              KeyStorageSettings storage) {

    public static KeyCoreSettings defaults() {
        return new KeyCoreSettings(
            Schema.COMMAND_ALIASES.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue(),
            KeyStorageSettings.defaults()
        );
    }

    public static KeyCoreSettings loadFrom(FileConfig config) {
        String[] commandAliases = config.getOrSet(Schema.COMMAND_ALIASES);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);

        KeyStorageSettings storage = KeyStorageSettings.loadFrom(config);

        return new KeyCoreSettings(commandAliases, dataSaveInterval, storage);
    }

    private static final class Schema {

        static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(
            ConfigCodecs.STRING_ARRAY,
            "command_aliases",
            new String[]{"cratekey", "cratekeys", "ckey", "casekey", "casekeys"},
            "The command aliases for key commands."
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            5,
            "The interval in seconds at which key data is saved."
        );
    }
}
