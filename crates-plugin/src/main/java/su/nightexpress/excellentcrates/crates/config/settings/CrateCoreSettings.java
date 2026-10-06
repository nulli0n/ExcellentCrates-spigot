package su.nightexpress.excellentcrates.crates.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record CrateCoreSettings(CrateModuleSettings modules, String[] commandAliases, int dataSaveInterval) {

    public static CrateCoreSettings defaults() {
        return new CrateCoreSettings(
            CrateModuleSettings.defaultSettings(),
            Schema.COMMAND_ALIASES.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue()
        );
    }

    public static CrateCoreSettings loadFrom(FileConfig config) {
        CrateModuleSettings modules = CrateModuleSettings.loadFrom(config);

        String[] commandAliases = config.getOrSet(Schema.COMMAND_ALIASES);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);

        return new CrateCoreSettings(modules, commandAliases, dataSaveInterval);
    }

    private static final class Schema {

        static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(
            ConfigCodecs.STRING_ARRAY,
            "command_aliases",
            new String[]{"crate", "crates", "case", "cases"},
            "The command aliases for crate commands."
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            5,
            "The interval in seconds at which crate data is saved."
        );
    }
}
