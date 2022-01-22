package su.nightexpress.excellentcrates.crate.effect;

import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.effect.list.*;

public enum CrateEffectModel {

    HELIX, PULSAR, BEACON, TORNADO, VORTEX, SIMPLE, NONE;

    private AbstractCrateBlockEffect effect;

    public static void start() {
        for (CrateEffectModel model : CrateEffectModel.values()) {
            if (model.effect != null && !model.effect.isCancelled()) continue;

            AbstractCrateBlockEffect effect = model.createEffect();
            if (effect == null) continue;

            effect.start();
        }
    }

    public static void shutdown() {
        for (CrateEffectModel model : CrateEffectModel.values()) {
            if (model.effect != null) {
                model.effect.cancel();
                model.effect = null;
            }
        }
    }

    @Nullable
    private AbstractCrateBlockEffect createEffect() {
        if (this.effect != null) return this.effect;

        return switch (this) {
            case PULSAR -> (this.effect = new CrateEffectPulsar());
            case HELIX -> (this.effect = new CrateEffectHelix());
            case BEACON -> (this.effect = new CrateEffectBeacon());
            case TORNADO -> (this.effect = new CrateEffectTornado());
            case VORTEX -> (this.effect = new CrateEffectVortex());
            case SIMPLE -> (this.effect = new CrateEffectSimple());
            default -> null;
        };
    }
}
