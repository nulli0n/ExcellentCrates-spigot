package su.nightexpress.excellentcrates.registry;

import org.checkerframework.checker.units.qual.Force;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.CostType;
import su.nightexpress.excellentcrates.crate.effect.CrateEffect;
import su.nightexpress.excellentcrates.crate.effect.impl.*;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerType;
import su.nightexpress.nightcore.bridge.registry.NightRegistry;

import java.util.Set;
import java.util.stream.Collectors;

public class CratesRegistries {

    public static final NightRegistry<String, CrateEffect> EFFECT = new NightRegistry<>();
    public static final NightRegistry<String, CostType> COST_TYPE = new NightRegistry<>();

    public static void load(@NotNull CratesPlugin plugin) {
        registerDefaultEffects();

    }

    private static void registerDefaultEffects() {
        registerEffect(new BeaconEffect());
        registerEffect(new HeartEffect());
        registerEffect(new HelixEffect());
        registerEffect(new PulsarEffect());
        registerEffect(new SimpleEffect());
        registerEffect(new SphereEffect());
        registerEffect(new SpiralEffect());
        registerEffect(new TornadoEffect());
        registerEffect(new VortexEffect());
        registerEffect(new AtomEffect());
        registerEffect(new CrystalEffect());
        registerEffect(new OrbitEffect());
        registerEffect(new CageEffect());
        registerEffect(new RingEffect());
        registerEffect(new HaloEffect());
        registerEffect(new FountainEffect());
        registerEffect(new BlackHoleEffect());
        registerEffect(new CelestialBandsEffect());
        registerEffect(new HeartOfTheForestEffect());
        registerEffect(new RitualEffect());
        registerEffect(new RainEffect());
        registerEffect(new InfinityEffect());
        registerEffect(new GalaxyEffect());
        registerEffect(new SpiritWellEffect());
        registerEffect(new ForceFieldEffect());
        registerEffect(new TwistedFieldEffect());
    }

    public static void clear() {
        COST_TYPE.clear();
        EFFECT.clear();
    }

    public static void registerEffect(@NotNull CrateEffect effect) {
        EFFECT.register(effect.getId(), effect);
    }

    public static void registerCostType(@NotNull CostType type) {
        COST_TYPE.register(type.getId(), type);
    }

    @NotNull
    public static CrateEffect effectOrDummy(@NotNull String id) {
        return EFFECT.lookup(id).orElse(DummyEffect.INSTANCE);
    }

    @NotNull
    public static Set<CrateEffect> getEffects() {
        return EFFECT.values();
    }

    @Nullable
    public static CostType getCostType(@NotNull String id) {
        return COST_TYPE.byKey(id);
    }

    @NotNull
    public static Set<CostType> getAvailableCostTypes() {
        return COST_TYPE.map().values().stream().filter(CostType::isAvailable).collect(Collectors.toSet());
    }
}
