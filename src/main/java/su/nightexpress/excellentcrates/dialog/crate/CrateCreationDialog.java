package su.nightexpress.excellentcrates.dialog.crate;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.dialog.generic.GenericCreationDialog;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class CrateCreationDialog extends GenericCreationDialog<CrateManager> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crates.Creation.Title").text(title("Crates", "Creation"));

    @Override
    @NotNull
    protected TextLocale title() {
        return TITLE;
    }

    @Override
    protected boolean canCreate(@NotNull CrateManager source, @NotNull String id) {
        return !source.hasCrate(id);
    }

    @Override
    protected void create(@NotNull CrateManager source, @NotNull String id) {
        source.createCrate(id);
    }
}
