package su.nightexpress.excellentcrates.api.crate.data.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface CrateBuilder {

    Crate build();

    CrateBuilder base(CrateBase base);

    CrateBuilder display(CrateDisplay display);

    CrateBuilder item(CrateItem item);

    <T extends CrateComponent> CrateBuilder component(EntityComponentKey<T> key, T component);
}
