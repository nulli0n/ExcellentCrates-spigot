package su.nightexpress.excellentcrates.preview.inventory.menu;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record InventoryMenuContext(Identifier crateId,
                                   BackwardNavigator backwardNavigator) implements BackwardSupport {

}
