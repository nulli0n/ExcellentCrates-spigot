package su.nightexpress.excellentcrates.api.crate.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;

@NullMarked
public interface CrateEditorExtension extends Identifiable {

    MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot);
}
