package su.nightexpress.excellentcrates.crates.cooldown.db;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record CrateCooldownDBSettings(int cacheTTL, String tableName, int dataSaveInterval) {

    public static CrateCooldownDBSettings defaultSettings() {
        return new CrateCooldownDBSettings(
            Schema.CACHE_TTL.getDefaultValue(),
            Schema.TABLE_NAME.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue()
        );
    }

    public static CrateCooldownDBSettings loadFrom(Path path) {
        FileConfig config = FileConfig.load(path);

        int cacheTTL = config.getOrSet(Schema.CACHE_TTL);
        String tableName = config.getOrSet(Schema.TABLE_NAME);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);

        config.saveChanges();

        return new CrateCooldownDBSettings(cacheTTL, tableName, dataSaveInterval);
    }

    private static final class Schema {

        static final ConfigProperty<Integer> CACHE_TTL = ConfigProperty.of(
            ConfigCodecs.INT,
            "cache_time_to_live",
            60,
            "The time-to-live for the cache in minutes."
        );

        static final ConfigProperty<String> TABLE_NAME = ConfigProperty.of(
            ConfigCodecs.STRING,
            "table_name",
            "crate_cooldowns",
            "The name of the database table for crate cooldowns."
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            5,
            "The interval in minutes at which the data should be saved to the database."
        );
    }
}
