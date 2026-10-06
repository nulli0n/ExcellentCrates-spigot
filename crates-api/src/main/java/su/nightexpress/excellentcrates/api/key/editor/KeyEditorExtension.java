package su.nightexpress.excellentcrates.api.key.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;

@NullMarked
public interface KeyEditorExtension extends Identifiable {

    MenuItem createButton(CrateKey key, KeyEditorHook hook, BackwardNavigator navigator, int slot);
}
