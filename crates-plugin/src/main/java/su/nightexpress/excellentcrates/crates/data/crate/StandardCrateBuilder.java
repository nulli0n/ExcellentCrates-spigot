package su.nightexpress.excellentcrates.crates.data.crate;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBuilder;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateItem;

@NullMarked
public class StandardCrateBuilder implements CrateBuilder {

    private final Identifier              id;
    final Map<Identifier, CrateComponent> components;

    CrateBase    base    = StandardCrateBase.createDefault();
    CrateDisplay display = StandardCrateDisplay.createDefault();
    CrateItem    item    = StandardCrateItem.createDefault();

    public StandardCrateBuilder(Identifier id) {
        this.id = id;
        this.components = new HashMap<>();
    }

    @Override
    public StandardCrate build() {
        return new StandardCrate(this.id, this);
    }

    @Override
    public StandardCrateBuilder base(CrateBase base) {
        this.base = base;
        return this;
    }

    @Override
    public CrateBuilder display(CrateDisplay display) {
        this.display = display;
        return this;
    }

    @Override
    public StandardCrateBuilder item(CrateItem item) {
        this.item = item;
        return this;
    }

    @Override
    public <T extends CrateComponent> CrateBuilder component(EntityComponentKey<T> key, T component) {
        this.components.put(key.id(), component);
        return this;
    }
}
