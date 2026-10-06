package su.nightexpress.excellentcrates.engine.module;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

@NullMarked
public record ModuleSettings(boolean rewardsEnabled,
                             boolean rarityEnabled,
                             boolean keysEnabled,
                             boolean animationsEnabled,
                             boolean costEnabled,
                             boolean previewEnabled,
                             boolean effectsEnabled) {

    public static ModuleSettings loadFrom(Path path) {
        FileConfig config = FileConfig.load(path);

        boolean rewardsEnabled = config.getOrSet(Schema.REWARDS_ENABLED);
        boolean rarityEnabled = config.getOrSet(Schema.RARITY_ENABLED);
        boolean keysEnabled = config.getOrSet(Schema.KEYS_ENABLED);
        boolean animationsEnabled = config.getOrSet(Schema.ANIMATIONS_ENABLED);
        boolean costEnabled = config.getOrSet(Schema.COST_ENABLED);
        boolean previewEnabled = config.getOrSet(Schema.PREVIEW_ENABLED);
        boolean effectsEnabled = config.getOrSet(Schema.EFFECTS_ENABLED);

        config.saveChanges();

        return new ModuleSettings(
            rewardsEnabled,
            rarityEnabled,
            keysEnabled,
            animationsEnabled,
            costEnabled,
            previewEnabled,
            effectsEnabled
        );
    }

    private static final class Schema {

        static final ConfigProperty<Boolean> REWARDS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.rewards",
            true,
            "Enable or disable rewards module"
        );

        static final ConfigProperty<Boolean> RARITY_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.rarity",
            true,
            "Enable or disable rarity module"
        );

        static final ConfigProperty<Boolean> KEYS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.keys",
            true,
            "Enable or disable keys module"
        );

        static final ConfigProperty<Boolean> ANIMATIONS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.animations",
            true,
            "Enable or disable animations module"
        );

        static final ConfigProperty<Boolean> COST_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.cost",
            true,
            "Enable or disable cost module"
        );

        static final ConfigProperty<Boolean> PREVIEW_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.preview",
            true,
            "Enable or disable preview module"
        );

        static final ConfigProperty<Boolean> EFFECTS_ENABLED = ConfigProperty.of(
            ConfigCodecs.BOOLEAN,
            "modules.effects",
            true,
            "Enable or disable effects module"
        );
    }
}
