package su.nightexpress.excellentcrates.animation;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.animation.AnimationProvider;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class DefaultAnimationRegistry implements AnimationRegistry {

    private final IdentifiableRegistry<AnimationProvider<?>> providers = new IdentifiableRegistry<>();
    private final KeyedRegistry<AnimationProfile>            profiles  = new KeyedRegistry<>();

    @Override
    public void clear() {
        this.clearProviders();
        this.clearProfiles();
    }

    @Override
    public void clearProviders() {
        this.providers.clear();
    }

    @Override
    public void clearProfiles() {
        this.profiles.clear();
    }

    @Override
    public @Nullable AnimationProfile getProfile(AdaptedKey key) {
        return this.profiles.get(key);
    }

    @Override
    public Set<AnimationProfile> getProfiles() {
        return this.profiles.values();
    }

    @Override
    public @Nullable AnimationProvider<?> getProvider(Identifier providerId) {
        return this.providers.get(providerId);
    }

    @Override
    public Set<AnimationProvider<?>> getProviders() {
        return this.providers.values();
    }

    @Override
    public void registerProfile(AnimationProfile config) {
        this.profiles.register(config);
    }

    @Override
    public void registerProvider(AnimationProvider<?> provider) {
        this.providers.register(provider);
    }
}
