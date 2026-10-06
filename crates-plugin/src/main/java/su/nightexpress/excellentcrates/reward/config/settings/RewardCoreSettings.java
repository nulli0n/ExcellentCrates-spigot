package su.nightexpress.excellentcrates.reward.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record RewardCoreSettings(RewardModuleSettings modules, String[] commandAliases, int dataSaveInterval) {

    public static RewardCoreSettings defaults() {
        return new RewardCoreSettings(
            RewardModuleSettings.defaultConfiguration(),
            Schema.COMMAND_ALIASES.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue()
        );
    }

    public static RewardCoreSettings loadFrom(FileConfig config) {
        RewardModuleSettings modules = RewardModuleSettings.loadFrom(config);

        String[] commandAliases = config.getOrSet(Schema.COMMAND_ALIASES);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);

        return new RewardCoreSettings(modules, commandAliases, dataSaveInterval);
    }

    private static final class Schema {

        static final ConfigProperty<String[]> COMMAND_ALIASES = ConfigProperty.of(
            ConfigCodecs.STRING_ARRAY,
            "command_aliases",
            new String[]{"cratereward", "craterewards", "creward", "casereward"},
            "The command aliases for reward commands."
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            5,
            "The interval in seconds at which reward data is saved."
        );
    }
}
