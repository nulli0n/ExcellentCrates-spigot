package su.nightexpress.excellentcrates.animation.style.simpleroll;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.AnimationContext;
import su.nightexpress.excellentcrates.api.animation.AnimationInstance;
import su.nightexpress.excellentcrates.core.animation.BaseAnimationProfile;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class SimpleRollProfile extends BaseAnimationProfile {

    private final SimpleRollSettings settings;

    public SimpleRollProfile(AdaptedKey key, SimpleRollSettings settings) {
        super(key);
        this.settings = settings;
    }

    @Override
    public String getName() {
        return this.settings.getName();
    }

    @Override
    public AnimationInstance createInstance(AnimationContext context, Runnable onComplete) {
        return new SimpleRollAnimation(this.settings, context, onComplete);
    }
}
