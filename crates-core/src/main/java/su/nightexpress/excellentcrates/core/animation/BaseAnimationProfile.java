package su.nightexpress.excellentcrates.core.animation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public abstract class BaseAnimationProfile implements AnimationProfile {

    protected final AdaptedKey key;

    public BaseAnimationProfile(AdaptedKey key) {
        this.key = key;
    }

    @Override
    public AdaptedKey getKey() {
        return this.key;
    }
}
