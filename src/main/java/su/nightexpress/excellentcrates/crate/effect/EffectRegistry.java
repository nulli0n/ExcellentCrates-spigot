package su.nightexpress.excellentcrates.crate.effect;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.effect.impl.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EffectRegistry {

    private static Map<String, CrateEffect> effectById;
    private static DummyEffect              dummyEffect;

    public static void load() {
        effectById = new HashMap<>();
        dummyEffect = new DummyEffect();

        register(EffectId.BEACON, new BeaconEffect());
        register(EffectId.HEART, new HeartEffect());
        register(EffectId.HELIX, new HelixEffect());
        register(EffectId.PULSAR, new PulsarEffect());
        register(EffectId.SIMPLE, new SimpleEffect());
        register(EffectId.SPHERE, new SphereEffect());
        register(EffectId.SPIRAL, new SpiralEffect());
        register(EffectId.TORNADO, new TornadoEffect());
        register(EffectId.VORTEX, new VortexEffect());
        register(EffectId.NONE, dummyEffect);
    }

    public static void clear() {
        effectById.clear();
        effectById = null;
        dummyEffect = null;
    }

    public static void register(@NotNull String name, @NotNull CrateEffect effect) {
        effectById.put(name.toLowerCase(), effect);
    }

    @NotNull
    public static Set<CrateEffect> getEffects() {
        return new HashSet<>(effectById.values());
    }

    @NotNull
    public static Set<String> getEffectNames() {
        return new HashSet<>(effectById.keySet());
    }

    @Nullable
    public static CrateEffect getEffectById(@NotNull String name) {
        return effectById.get(name.toLowerCase());
    }

    @NotNull
    public static CrateEffect getEffectOrDummy(@NotNull String name) {
        return effectById.getOrDefault(name.toLowerCase(), dummyEffect);
    }
}
