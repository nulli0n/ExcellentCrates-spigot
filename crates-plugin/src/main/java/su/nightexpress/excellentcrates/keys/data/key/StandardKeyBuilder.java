package su.nightexpress.excellentcrates.keys.data.key;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.data.KeyComponent;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBase;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBuilder;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;

@NullMarked
public class StandardKeyBuilder implements KeyBuilder {

    private final Identifier id;

    final Map<Identifier, KeyComponent> components;

    KeyBase    base;
    KeyDisplay display;
    KeyItem    item;

    public StandardKeyBuilder(Identifier id) {
        this.id = id;
        this.base = StandardKeyBase.createDefault();
        this.display = StandardKeyDisplay.createDefault();
        this.item = StandardKeyItem.createDefault();
        this.components = new HashMap<>();
    }

    @Override
    public StandardKeyBuilder base(KeyBase base) {
        this.base = base;
        return this;
    }

    @Override
    public StandardKeyBuilder display(KeyDisplay display) {
        this.display = display;
        return this;
    }

    @Override
    public StandardKeyBuilder item(KeyItem item) {
        this.item = item;
        return this;
    }

    @Override
    public <T extends KeyComponent> KeyBuilder component(EntityComponentKey<T> key, T component) {
        this.components.put(key.id(), component);
        return this;
    }


    @Override
    public DefaultCrateKey build() {
        return new DefaultCrateKey(this.id, this);
    }
}
