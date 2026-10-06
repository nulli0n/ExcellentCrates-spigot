package su.nightexpress.excellentcrates.animation.style.csgo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationProfile;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class CSGOAnimationProfile extends BaseAnimationProfile {

    private final CSGOAnimationSettings settings;

    public CSGOAnimationProfile(AdaptedKey key, CSGOAnimationSettings settings) {
        super(key);
        this.settings = settings;
    }

    @Override
    public AnimationInstance createInstance(AnimationContext context, Runnable onComplete) {
        return new CSGOAnimationInstance(context, onComplete, this.settings);
    }

    @Override
    public String getName() {
        return this.settings.name();
    }
}
