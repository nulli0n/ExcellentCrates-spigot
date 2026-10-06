package su.nightexpress.excellentcrates.reward.preview.settings;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bukkit.codec.BukkitColorCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record RewardPreviewSettings(int previewCacheTTL, Color defaultRewardColor) {

    public static RewardPreviewSettings defaults() {
        return new RewardPreviewSettings(
            Schema.PREVIEW_CACHE_TTL.getDefaultValue(),
            Schema.DEFAULT_REWARD_COLOR.getDefaultValue()
        );
    }

    public static RewardPreviewSettings loadFrom(FileConfig config) {
        int previewCacheTTL = config.getOrSet(Schema.PREVIEW_CACHE_TTL);
        Color defaultRewardColor = config.getOrSet(Schema.DEFAULT_REWARD_COLOR);

        return new RewardPreviewSettings(previewCacheTTL, defaultRewardColor);
    }

    private static final class Schema {

        static final ConfigProperty<Integer> PREVIEW_CACHE_TTL = ConfigProperty.of(
            ConfigCodecs.INT,
            "cache-ttl",
            30,
            "Sets the time-to-live (TTL) for the preview cache in minutes."
        );

        static final ConfigProperty<Color> DEFAULT_REWARD_COLOR = ConfigProperty.of(
            BukkitColorCodec.INSTANCE,
            "default-reward-color",
            Color.WHITE,
            "Sets the default color for rewards in the preview."
        );
    }
}
