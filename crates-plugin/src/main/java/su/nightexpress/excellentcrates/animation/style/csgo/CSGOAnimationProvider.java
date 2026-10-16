package su.nightexpress.excellentcrates.animation.style.csgo;

import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.animation.style.csgo.codec.CSGOAnimationSettingsCodec;
import su.nightexpress.excellentcrates.api.animation.AnimationProvider;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class CSGOAnimationProvider implements AnimationProvider<CSGOAnimationProfile> {

    private static final Identifier ID = new Identifier("csgo_animation");

    private final Plugin plugin;

    public CSGOAnimationProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public CSGOAnimationProfile readProfile(AdaptedKey key, FileConfig config) {
        CSGOAnimationSettings settings = config.getOrSet("settings",
            CSGOAnimationSettingsCodec.INSTANCE,
            CSGOAnimationSettings.defaultSettings()
        );

        return new CSGOAnimationProfile(this.plugin, key, settings);
    }

    @Override
    public void writeDefaultProfile(AdaptedKey key, FileConfig config) {
        config.set("settings", CSGOAnimationSettings.defaultSettings());
    }

    @Override
    public Identifier getId() {
        return ID;
    }
}
