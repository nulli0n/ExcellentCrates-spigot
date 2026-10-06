package su.nightexpress.excellentcrates.crates.data.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ImmutableEntityComponentContainer;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;

@NullMarked
public class DefaultCrate implements Crate {

    private final Identifier id;

    private final ICrateBase    base;
    private final ICrateDisplay display;
    private final ICrateItem    item;

    private final ImmutableEntityComponentContainer<CrateComponent> components;

    public DefaultCrate(Identifier id, CrateBuilder builder) {
        this.id = id;
        this.base = builder.base;
        this.display = builder.display;
        this.item = builder.item;
        this.components = new ImmutableEntityComponentContainer<>(builder.components);
    }

    @Override
    public ImmutableEntityComponentContainer<CrateComponent> getComponents() {
        return this.components;
    }

    @Override
    public ICrateBase getBase() {
        return this.base;
    }

    @Override
    public ICrateDisplay getDisplay() {
        return this.display;
    }

    @Override
    public ICrateItem getItem() {
        return this.item;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }
}
