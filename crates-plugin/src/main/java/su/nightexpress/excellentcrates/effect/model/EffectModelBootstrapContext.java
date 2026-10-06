package su.nightexpress.excellentcrates.effect.model;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.effect.EffectModel;
import su.nightexpress.excellentcrates.effect.model.beacon.BeaconEffect;
import su.nightexpress.excellentcrates.effect.model.helix.HelixEffectModel;
import su.nightexpress.excellentcrates.effect.model.pulsar.PulsarEffect;
import su.nightexpress.excellentcrates.effect.model.sphere.SphereEffect;
import su.nightexpress.excellentcrates.effect.model.spiral.SpiralEffect;
import su.nightexpress.excellentcrates.effect.model.vortex.VortexEffect;

@NullMarked
public class EffectModelBootstrapContext {

    private final List<EffectModel<?>> models;

    public EffectModelBootstrapContext() {
        this.models = new ArrayList<>();

        this.models.add(new BeaconEffect());
        this.models.add(new HelixEffectModel());
        this.models.add(new PulsarEffect());
        this.models.add(new SphereEffect());
        this.models.add(new SpiralEffect());
        this.models.add(new VortexEffect());
    }

    public List<EffectModel<?>> getModels() {
        return this.models;
    }
}
