package su.nightexpress.excellentcrates.crates.data.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ImmutableEntityComponentContainer;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateItem;

@NullMarked
public class StandardCrate implements Crate {

    private final Identifier id;

    private final CrateBase    base;
    private final CrateDisplay display;
    private final CrateItem    item;

    private final ImmutableEntityComponentContainer<CrateComponent> components;

    public StandardCrate(Identifier id, StandardCrateBuilder builder) {
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
    public CrateBase getBase() {
        return this.base;
    }

    @Override
    public CrateDisplay getDisplay() {
        return this.display;
    }

    @Override
    public CrateItem getItem() {
        return this.item;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }
}
