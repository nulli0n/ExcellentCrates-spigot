package su.nightexpress.excellentcrates.api.effect;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface EffectRegistry {

    void clear();

    void clearProfiles();

    void clearModels();

    void registerModel(EffectModel<?> model);

    void registerProfile(EffectProfile<?> profile);

    @Nullable
    EffectModel<?> getModel(Identifier id);

    @Nullable
    EffectProfile<?> getProfile(AdaptedKey key);

    Set<EffectModel<?>> getModels();

    Set<EffectProfile<?>> getProfiles();
}
