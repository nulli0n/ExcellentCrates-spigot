package su.nightexpress.excellentcrates.crates.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record CrateModuleSettings(boolean blocksEnabled,
                                  boolean hologramsEnabled,
                                  boolean cooldownsEnabled,
                                  boolean historyEnabled,
                                  boolean batchEnabled) {

    public static CrateModuleSettings defaultSettings() {
        return new CrateModuleSettings(
            Schema.BLOCKS_ENABLED.getDefaultValue(),
            Schema.HOLOGRAMS_ENABLED.getDefaultValue(),
            Schema.COOLDOWNS_ENABLED.getDefaultValue(),
            Schema.HISTORY_ENABLED.getDefaultValue(),
            Schema.BATCH_ENABLED.getDefaultValue()
        );
    }

    public static CrateModuleSettings loadFrom(FileConfig config) {
        boolean blocksEnabled = config.getOrSet(Schema.BLOCKS_ENABLED);
        boolean hologramsEnabled = config.getOrSet(Schema.HOLOGRAMS_ENABLED);
        boolean cooldownsEnabled = config.getOrSet(Schema.COOLDOWNS_ENABLED);
        boolean historyEnabled = config.getOrSet(Schema.HISTORY_ENABLED);
        boolean batchEnabled = config.getOrSet(Schema.BATCH_ENABLED);

        return new CrateModuleSettings(
            blocksEnabled,
            hologramsEnabled,
            cooldownsEnabled,
            historyEnabled,
            batchEnabled
        );
    }

    private static final class Schema {

        static final ConfigProperty<Boolean> BLOCKS_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "blocks-enabled",
            true,
            "Enable or disable blocks feature"
        );

        static final ConfigProperty<Boolean> HOLOGRAMS_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "holograms-enabled",
            true,
            "Enable or disable holograms feature"
        );

        static final ConfigProperty<Boolean> COOLDOWNS_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "cooldowns-enabled",
            true,
            "Enable or disable cooldowns feature"
        );

        static final ConfigProperty<Boolean> HISTORY_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "history-enabled",
            true,
            "Enable or disable history feature"
        );

        static final ConfigProperty<Boolean> BATCH_ENABLED = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "batch-enabled",
            true,
            "Enable or disable batch feature"
        );
    }
}
