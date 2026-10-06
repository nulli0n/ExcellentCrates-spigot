package su.nightexpress.excellentcrates.reward.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record RewardModuleSettings(boolean broadcastEnabled,
                                   boolean commandsEnabled,
                                   boolean itemsEnabled,
                                   boolean cooldownsEnabled,
                                   boolean limitsEnabled,
                                   boolean selectionEnabled) {

    public static RewardModuleSettings defaultConfiguration() {
        return new RewardModuleSettings(
            Schema.BROADCAST_ENABLED.getDefaultValue(),
            Schema.COMMANDS_ENABLED.getDefaultValue(),
            Schema.ITEMS_ENABLED.getDefaultValue(),
            Schema.COOLDOWNS_ENABLED.getDefaultValue(),
            Schema.LIMITS_ENABLED.getDefaultValue(),
            Schema.SELECTION_ENABLED.getDefaultValue()
        );
    }

    public static RewardModuleSettings loadFrom(FileConfig config) {
        boolean broadcastEnabled = config.getOrSet(Schema.BROADCAST_ENABLED);
        boolean commandsEnabled = config.getOrSet(Schema.COMMANDS_ENABLED);
        boolean itemsEnabled = config.getOrSet(Schema.ITEMS_ENABLED);
        boolean cooldownsEnabled = config.getOrSet(Schema.COOLDOWNS_ENABLED);
        boolean limitsEnabled = config.getOrSet(Schema.LIMITS_ENABLED);
        boolean selectionEnabled = config.getOrSet(Schema.SELECTION_ENABLED);

        return new RewardModuleSettings(
            broadcastEnabled,
            commandsEnabled,
            itemsEnabled,
            cooldownsEnabled,
            limitsEnabled,
            selectionEnabled
        );
    }

    private static final class Schema {

        static final ConfigProperty<Boolean> BROADCAST_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.broadcast-enabled",
            true,
            "Enable or disable broadcast for rewards."
        );

        static final ConfigProperty<Boolean> COMMANDS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.commands-enabled",
            true,
            "Enable or disable commands for rewards."
        );

        static final ConfigProperty<Boolean> ITEMS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.items-enabled",
            true,
            "Enable or disable items for rewards."
        );

        static final ConfigProperty<Boolean> COOLDOWNS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.cooldowns-enabled",
            true,
            "Enable or disable cooldowns for rewards."
        );

        static final ConfigProperty<Boolean> LIMITS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.limits-enabled",
            true,
            "Enable or disable limits for rewards."
        );

        static final ConfigProperty<Boolean> SELECTION_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "features.selection-enabled",
            true,
            "Enable or disable selective rewards."
        );
    }
}
