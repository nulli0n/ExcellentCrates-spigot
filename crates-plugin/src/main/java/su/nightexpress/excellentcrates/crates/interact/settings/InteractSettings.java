package su.nightexpress.excellentcrates.crates.interact.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record InteractSettings(boolean cooldownEnabled, int cooldownDuration) {

    public static InteractSettings defaults() {
        return new InteractSettings(
            Schema.COOLDOWN_ENABLED.getDefaultValue(),
            Schema.COOLDOWN_DURATION.getDefaultValue()
        );
    }

    public static InteractSettings loadFrom(FileConfig config) {
        boolean cooldownEnabled = config.getOrSet(Schema.COOLDOWN_ENABLED);
        int cooldownDuration = config.getOrSet(Schema.COOLDOWN_DURATION);

        return new InteractSettings(cooldownEnabled, cooldownDuration);
    }

    private static final class Schema {

        static final ConfigProperty<Boolean> COOLDOWN_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "cooldown.enabled",
            true,
            "Whether the cooldown is enabled for interactions."
        );

        static final ConfigProperty<Integer> COOLDOWN_DURATION = ConfigProperty.of(
            ConfigCodecs.INT,
            "cooldown.duration",
            3,
            "The duration of the cooldown (in seconds) for interactions."
        );
    }
}
