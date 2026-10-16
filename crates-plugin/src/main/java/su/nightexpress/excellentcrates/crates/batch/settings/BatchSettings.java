package su.nightexpress.excellentcrates.crates.batch.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record BatchSettings(int maxBatchSize,
                            boolean triggerInFastModeOnly,
                            boolean fastModeShowSelection) {

    public static BatchSettings defaultSettings() {
        return new BatchSettings(
            Schema.MAX_BATCH_SIZE.getDefaultValue(),
            Schema.TRIGGER_IN_FAST_MODE_ONLY.getDefaultValue(),
            Schema.FAST_MODE_SHOW_SELECTION.getDefaultValue()
        );
    }

    public static BatchSettings loadFrom(FileConfig config) {
        int maxBatchSize = config.getOrSet(Schema.MAX_BATCH_SIZE);
        boolean triggerInFastModeOnly = config.getOrSet(Schema.TRIGGER_IN_FAST_MODE_ONLY);
        boolean fastModeShowSelection = config.getOrSet(Schema.FAST_MODE_SHOW_SELECTION);

        return new BatchSettings(maxBatchSize, triggerInFastModeOnly, fastModeShowSelection);
    }

    private static final class Schema {

        static final ConfigProperty<Integer> MAX_BATCH_SIZE = ConfigProperty.of(ConfigCodecs.INT,
            "max_batch_size",
            20,
            "Sets the maximum batch size."
        );

        static final ConfigProperty<Boolean> TRIGGER_IN_FAST_MODE_ONLY = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "trigger_in_fast_mode_only",
            true,
            "Whether to trigger in fast mode only."
        );

        static final ConfigProperty<Boolean> FAST_MODE_SHOW_SELECTION = ConfigProperty.of(ConfigCodecs.BOOLEAN,
            "fast_mode_show_selection",
            true,
            "Whether to show selection in fast mode."
        );
    }
}
