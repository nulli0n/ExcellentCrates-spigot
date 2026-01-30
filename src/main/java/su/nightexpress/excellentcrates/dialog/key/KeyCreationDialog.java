package su.nightexpress.excellentcrates.dialog.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.dialog.generic.GenericCreationDialog;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class KeyCreationDialog extends GenericCreationDialog<KeyManager> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Key.Creation.Title").text(title("Key", "Creation"));

    @Override
    protected @NotNull TextLocale title() {
        return TITLE;
    }

    @Override
    protected boolean canCreate(@NotNull KeyManager manager, @NotNull String id) {
        return !manager.hasKey(id);
    }

    @Override
    protected void create(@NotNull KeyManager manager, @NotNull String id) {
        manager.createKey(id);
    }
}
