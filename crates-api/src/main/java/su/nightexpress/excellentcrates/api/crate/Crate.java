package su.nightexpress.excellentcrates.api.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ComponentEntity;
import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateItem;

@NullMarked
public interface Crate extends ComponentEntity<CrateComponent>, Identifiable {

    CrateBase getBase();

    CrateDisplay getDisplay();

    CrateItem getItem();
}
