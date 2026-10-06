package su.nightexpress.excellentcrates.keys.data.key;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ImmutableEntityComponentContainer;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.KeyComponent;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBase;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;

@NullMarked
public class DefaultCrateKey implements CrateKey {

    private final Identifier id;
    private final KeyBase    base;
    private final KeyDisplay display;
    private final KeyItem    item;

    private final ImmutableEntityComponentContainer<KeyComponent> components;

    public DefaultCrateKey(Identifier id, StandardKeyBuilder builder) {
        this.id = id;
        this.base = builder.base;
        this.display = builder.display;
        this.item = builder.item;
        this.components = new ImmutableEntityComponentContainer<>(builder.components);
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public KeyBase getBase() {
        return base;
    }

    @Override
    public KeyDisplay getDisplay() {
        return display;
    }

    @Override
    public KeyItem getItem() {
        return item;
    }

    @Override
    public ImmutableEntityComponentContainer<KeyComponent> getComponents() {
        return this.components;
    }
}
