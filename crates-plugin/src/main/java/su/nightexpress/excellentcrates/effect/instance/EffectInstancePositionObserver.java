package su.nightexpress.excellentcrates.effect.instance;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePosition;
import su.nightexpress.excellentcrates.api.crate.block.position.CratePositionObserver;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.effect.instance.impl.DefaultEffectInstance;

@NullMarked
public class EffectInstancePositionObserver implements CratePositionObserver {

    private final CrateResolver         crateResolver;
    private final EffectInstanceService instanceService;

    public EffectInstancePositionObserver(CrateResolver crateResolver, EffectInstanceService instanceService) {
        this.crateResolver = crateResolver;
        this.instanceService = instanceService;
    }

    @Override
    public void onPositionAdded(Identifier crateId, CratePosition position) {
        Crate crate = this.crateResolver.resolveCrate(crateId);
        if (crate == null) return;

        this.instanceService.registerInstance(position, new DefaultEffectInstance(crate, position));
    }

    @Override
    public void onPositionRemoved(Identifier crateId, CratePosition position) {
        // Remove the effect instance associated with this position
        this.instanceService.unregisterInstance(position);
    }
}
