package su.nightexpress.excellentcrates.effect.instance;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.effect.instance.controller.EffectInstanceTickController;

@NullMarked
public class EffectInstanceBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("effects.instance");
    private static final String     NAME = "Instance";

    public final EffectInstanceService instanceService;

    private final EffectInstancePositionObserver positionObserver;

    public EffectInstanceBootstrapContext(CratesPlugin plugin,
                                          CrateRegistry crateRegistry,
                                          EffectRegistry effectRegistry) {
        super(ID, NAME);

        this.instanceService = new EffectInstanceService(effectRegistry);
        this.positionObserver = new EffectInstancePositionObserver(crateRegistry, this.instanceService);

        this.addComponent(new EffectInstanceTickController(plugin, instanceService));
    }

    public EffectInstancePositionObserver getPositionObserver() {
        return this.positionObserver;
    }
}
