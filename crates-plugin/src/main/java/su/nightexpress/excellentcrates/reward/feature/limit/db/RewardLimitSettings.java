package su.nightexpress.excellentcrates.reward.feature.limit.db;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record RewardLimitSettings(String tableName, int cacheTTL, int dataSaveInterval) {

    public static RewardLimitSettings defaults() {
        return new RewardLimitSettings(
            Schema.TABLE_NAME.getDefaultValue(),
            Schema.CACHE_TTL.getDefaultValue(),
            Schema.DATA_SAVE_INTERVAL.getDefaultValue()
        );
    }

    public static RewardLimitSettings loadFrom(FileConfig config) {
        int cacheTTL = config.getOrSet(Schema.CACHE_TTL);
        String tableName = config.getOrSet(Schema.TABLE_NAME);
        int dataSaveInterval = config.getOrSet(Schema.DATA_SAVE_INTERVAL);

        return new RewardLimitSettings(tableName, cacheTTL, dataSaveInterval);
    }

    private static final class Schema {

        static final ConfigProperty<String> TABLE_NAME = ConfigProperty.of(
            ConfigCodecs.STRING,
            "table_name",
            "reward_limits",
            "Database table name for reward limits.",
            "[Default: reward_limits]"
        );

        static final ConfigProperty<Integer> CACHE_TTL = ConfigProperty.of(
            ConfigCodecs.INT,
            "cache_time_to_live",
            60,
            "Cache time-to-live in minutes.",
            "[Default: 60 minutes]"
        );

        static final ConfigProperty<Integer> DATA_SAVE_INTERVAL = ConfigProperty.of(
            ConfigCodecs.INT,
            "data_save_interval",
            15,
            "Data save interval in seconds.",
            "[Default: 15 s]"
        );
    }
}
