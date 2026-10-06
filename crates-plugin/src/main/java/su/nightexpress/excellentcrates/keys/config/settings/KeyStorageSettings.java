package su.nightexpress.excellentcrates.keys.config.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record KeyStorageSettings(String tableName, int dataSaveInterval, int dataCacheTTL) {

    public static KeyStorageSettings defaults() {
        return new KeyStorageSettings(
            Schema.TABLE_NAME.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue(),
            Schema.DATA_CACHE_TTL.getDefaultValue()
        );
    }

    public static KeyStorageSettings loadFrom(FileConfig config) {
        String tableName = config.getOrSet(Schema.TABLE_NAME);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);
        int dataCacheTTL = config.getOrSet(Schema.DATA_CACHE_TTL);

        return new KeyStorageSettings(tableName, dataSaveInterval, dataCacheTTL);
    }

    private static final class Schema {

        static final ConfigProperty<String> TABLE_NAME = ConfigProperty.of(
            ConfigCodecs.STRING,
            "table_name",
            "key_storage",
            "The name of the table used for storing key data."
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            15,
            "The interval in seconds at which key data is saved."
        );

        static final ConfigProperty<Integer> DATA_CACHE_TTL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_cache_ttl",
            15,
            "The time-to-live in minutes for the data cache."
        );
    }
}
