package su.nightexpress.excellentcrates.crates.batch.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record BatchSettings(int maxBatchSize) {

    public static BatchSettings defaultSettings() {
        return new BatchSettings(Schema.MAX_BATCH_SIZE.getDefaultValue());
    }

    public static BatchSettings loadFrom(FileConfig config) {
        int maxBatchSize = config.getOrSet(Schema.MAX_BATCH_SIZE);

        return new BatchSettings(maxBatchSize);
    }

    private static final class Schema {

        static final ConfigProperty<Integer> MAX_BATCH_SIZE = ConfigProperty.of(ConfigCodecs.INT,
            "max_batch_size",
            20,
            "Sets the maximum batch size."
        );
    }
}
