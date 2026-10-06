package su.nightexpress.excellentcrates.api.key;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ComponentEntity;
import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.key.data.KeyComponent;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBase;
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;

@NullMarked
public interface CrateKey extends ComponentEntity<KeyComponent>, Identifiable {

    KeyBase getBase();

    KeyDisplay getDisplay();

    KeyItem getItem();
}
