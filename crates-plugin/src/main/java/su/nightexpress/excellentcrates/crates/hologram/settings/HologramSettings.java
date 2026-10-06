package su.nightexpress.excellentcrates.crates.hologram.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.core.codec.IdentifierCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record HologramSettings(Identifier providerId, long updateInterval) {

    public static final HologramSettings DEFAULT = new HologramSettings(
        Schema.PROVIDER_ID.getDefaultValue(),
        Schema.UPDATE_INTERVAL.getDefaultValue()
    );

    public static HologramSettings loadFrom(FileConfig config) {
        Identifier providerId = config.getOrSet(Schema.PROVIDER_ID);
        long updateInterval = config.getOrSet(Schema.UPDATE_INTERVAL);

        return new HologramSettings(providerId, updateInterval);
    }

    private static final class Schema {

        static final ConfigProperty<Identifier> PROVIDER_ID = ConfigProperty.of(
            IdentifierCodec.INSTANCE,
            "provider_id",
            new Identifier("packetevents"),
            "The identifier of the hologram provider to use. This should match the ID of a registered provider."
        );

        static final ConfigProperty<Long> UPDATE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.LONG,
            "update_interval",
            60L,
            "The interval in ticks at which the hologram display should be updated. A lower value means more frequent updates."
        );
    }
}
