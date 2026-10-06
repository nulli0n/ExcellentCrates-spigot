package su.nightexpress.excellentcrates.api.animation;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface AnimationRegistry {

    void clear();

    void clearProviders();

    void clearProfiles();

    void registerProvider(AnimationProvider<?> provider);

    void registerProfile(AnimationProfile profile);

    Set<AnimationProvider<?>> getProviders();

    Set<AnimationProfile> getProfiles();

    @Nullable
    AnimationProvider<?> getProvider(Identifier providerId);

    @Nullable
    AnimationProfile getProfile(AdaptedKey key);
}
