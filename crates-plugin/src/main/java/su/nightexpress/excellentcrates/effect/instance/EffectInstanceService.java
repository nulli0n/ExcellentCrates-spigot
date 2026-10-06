package su.nightexpress.excellentcrates.effect.instance;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.effect.EffectInstance;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;

@NullMarked
public class EffectInstanceService {

    private final EffectRegistry effectRegistry;

    // Using ConcurrentHashMap ensures thread safety if positions are added/removed asymmetrically 
    private final Map<CratePosition, EffectInstance> activeEffects = new ConcurrentHashMap<>();

    public EffectInstanceService(EffectRegistry effectRegistry) {
        this.effectRegistry = effectRegistry;
    }

    public void tick() {
        for (EffectInstance instance : this.activeEffects.values()) {
            instance.tick(this.effectRegistry);
        }
    }

    public void registerInstance(CratePosition position, EffectInstance instance) {
        this.activeEffects.put(position, instance);
    }

    public void unregisterInstance(CratePosition position) {
        this.activeEffects.remove(position);
    }
}
