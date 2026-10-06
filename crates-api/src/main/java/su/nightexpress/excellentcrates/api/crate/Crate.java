package su.nightexpress.excellentcrates.api.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ComponentEntity;
import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateBase;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateItem;

@NullMarked
public interface Crate extends ComponentEntity<CrateComponent>, Identifiable {

    ICrateBase getBase();

    ICrateDisplay getDisplay();

    ICrateItem getItem();
}
