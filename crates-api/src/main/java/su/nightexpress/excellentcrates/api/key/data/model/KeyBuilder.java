package su.nightexpress.excellentcrates.api.key.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.KeyComponent;

@NullMarked
public interface KeyBuilder {

    KeyBuilder base(KeyBase base);

    KeyBuilder display(KeyDisplay display);

    KeyBuilder item(KeyItem item);

    <T extends KeyComponent> KeyBuilder component(EntityComponentKey<T> key, T component);

    CrateKey build();
}
