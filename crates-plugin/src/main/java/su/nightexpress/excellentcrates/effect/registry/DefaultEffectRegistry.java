package su.nightexpress.excellentcrates.effect.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.effect.EffectModel;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyedRegistry;

@NullMarked
public class DefaultEffectRegistry implements EffectRegistry {

    private final IdentifiableRegistry<EffectModel<?>> models;
    private final KeyedRegistry<EffectProfile<?>>      profiles;

    public DefaultEffectRegistry() {
        this.models = new IdentifiableRegistry<>();
        this.profiles = new KeyedRegistry<>();
    }

    @Override
    public void clear() {
        // Clear both models and profiles
        this.models.clear();
        this.profiles.clear();
    }

    @Override
    public void clearModels() {
        // Clear only the models
        this.models.clear();
    }

    @Override
    public void clearProfiles() {
        // Clear only the profiles
        this.profiles.clear();
    }

    @Override
    public @Nullable EffectModel<?> getModel(Identifier id) {
        // Retrieve the model by its identifier
        return this.models.get(id);
    }

    @Override
    public Set<EffectModel<?>> getModels() {
        // Retrieve all models
        return this.models.values();
    }

    @Override
    public @Nullable EffectProfile<?> getProfile(AdaptedKey key) {
        // Retrieve the profile by its key
        return this.profiles.get(key);
    }

    @Override
    public Set<EffectProfile<?>> getProfiles() {
        // Retrieve all profiles
        return this.profiles.values();
    }

    @Override
    public void registerModel(EffectModel<?> model) {
        // Register the model
        this.models.register(model);
    }

    @Override
    public void registerProfile(EffectProfile<?> profile) {
        // Register the profile
        this.profiles.register(profile);
    }
}
