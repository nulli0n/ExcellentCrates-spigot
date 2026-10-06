package su.nightexpress.excellentcrates.crates.data.crate;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBuilder;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;

@NullMarked
public class CrateBuilder implements ICrateBuilder {

    private final Identifier              id;
    final Map<Identifier, CrateComponent> components;

    ICrateBase    base    = CrateBase.createDefault();
    ICrateDisplay display = CrateDisplay.createDefault();
    ICrateItem    item    = CrateItem.createDefault();

    public CrateBuilder(Identifier id) {
        this.id = id;
        this.components = new HashMap<>();
    }

    @Override
    public DefaultCrate build() {
        return new DefaultCrate(this.id, this);
    }

    @Override
    public CrateBuilder base(ICrateBase base) {
        this.base = base;
        return this;
    }

    @Override
    public ICrateBuilder display(ICrateDisplay display) {
        this.display = display;
        return this;
    }

    @Override
    public CrateBuilder item(ICrateItem item) {
        this.item = item;
        return this;
    }

    @Override
    public <T extends CrateComponent> ICrateBuilder component(EntityComponentKey<T> key, T component) {
        this.components.put(key.id(), component);
        return this;
    }
}
