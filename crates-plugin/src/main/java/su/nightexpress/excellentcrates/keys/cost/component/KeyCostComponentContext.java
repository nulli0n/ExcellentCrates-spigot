package su.nightexpress.excellentcrates.keys.cost.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.keys.cost.component.codec.KeyCostComponentCodec;
import su.nightexpress.excellentcrates.keys.cost.component.codec.KeyCostEntryCodec;
import su.nightexpress.excellentcrates.keys.cost.component.extension.KeyCostComponentExtension;
import su.nightexpress.excellentcrates.keys.cost.component.model.DefaultKeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.cost.component.model.StandardKeyRequirementEntry;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class KeyCostComponentContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.cost.component");
    private static final String     NAME = "Cost Component";

    private final KeyCostComponentExtension dataExtension;

    public KeyCostComponentContext() {
        super(ID, NAME);

        ConfigCodecs.register(StandardKeyRequirementEntry.class, KeyCostEntryCodec.INSTANCE);
        ConfigCodecs.register(DefaultKeyRequirementComponent.class, KeyCostComponentCodec.INSTANCE);

        this.dataExtension = new KeyCostComponentExtension();
    }

    public KeyCostComponentExtension getDataExtension() {
        return this.dataExtension;
    }
}
