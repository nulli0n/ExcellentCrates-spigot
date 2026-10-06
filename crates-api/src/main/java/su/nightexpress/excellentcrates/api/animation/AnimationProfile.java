package su.nightexpress.excellentcrates.api.animation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.bridge.key.KeyHolder;

@NullMarked
public interface AnimationProfile extends KeyHolder {

    String getName();

    AnimationInstance createInstance(AnimationContext context, Runnable onComplete);
}
