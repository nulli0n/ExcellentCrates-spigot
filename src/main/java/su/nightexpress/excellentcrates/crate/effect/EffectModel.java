package su.nightexpress.excellentcrates.crate.effect;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.effect.impl.*;

public enum EffectModel {

    HELIX(new CrateHelixEffect()),
    SPIRAL(new CrateSpiralEffect()),
    SPHERE(new CrateSphereEffect()),
    HEART(new CrateHeartEffect()),
    PULSAR(new CratePulsarEffect()),
    BEACON(new CrateBeaconEffect()),
    TORNADO(new CrateTornadoEffect()),
    VORTEX(new CrateVortexEffect()),
    SIMPLE(new CrateSimpleEffect()),
    NONE(new CrateSimpleEffect());

    private final AbstractEffect effect;

    EffectModel(@NotNull AbstractEffect effect) {
        this.effect = effect;
    }

    @NotNull
    public AbstractEffect getEffect() {
        return effect;
    }
}
