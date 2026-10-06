package su.nightexpress.excellentcrates.animation.style.csgo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.animation.style.csgo.codec.CSGOAnimationSettingsCodec;
import su.nightexpress.excellentcrates.api.animation.AnimationProvider;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class CSGOAnimationProvider implements AnimationProvider<CSGOAnimationProfile> {

    private static final Identifier ID = new Identifier("csgo_animation");

    @Override
    public CSGOAnimationProfile readProfile(AdaptedKey key, FileConfig config) {
        CSGOAnimationSettings settings = config.getOrSet("settings",
            CSGOAnimationSettingsCodec.INSTANCE,
            CSGOAnimationSettings.defaultSettings()
        );

        return new CSGOAnimationProfile(key, settings);
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
