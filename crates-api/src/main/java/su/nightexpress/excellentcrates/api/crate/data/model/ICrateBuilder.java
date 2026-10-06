package su.nightexpress.excellentcrates.api.crate.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface ICrateBuilder {

    Crate build();

    ICrateBuilder base(ICrateBase base);

    ICrateBuilder display(ICrateDisplay display);

    ICrateBuilder item(ICrateItem item);

    <T extends CrateComponent> ICrateBuilder component(EntityComponentKey<T> key, T component);
}
