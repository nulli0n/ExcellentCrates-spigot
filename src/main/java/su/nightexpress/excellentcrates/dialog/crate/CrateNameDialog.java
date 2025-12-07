package su.nightexpress.excellentcrates.dialog.crate;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.generic.GenericNameDialog;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class CrateNameDialog extends GenericNameDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Name.Title").text(title("Crate", "Name"));

    @Override
    @NotNull
    protected TextLocale title() {
        return TITLE;
    }

    @Override
    @NotNull
    protected AdaptedItem getItem(@NotNull Crate source) {
        return source.getItem();
    }

    @Override
    protected void setItem(@NotNull Crate source, @NotNull AdaptedItem item) {
        source.setItem(item);
    }

    @Override
    @NotNull
    protected String getName(@NotNull Crate source) {
        return source.getName();
    }

    @Override
    protected void setName(@NotNull Crate source, @NotNull String name) {
        source.setName(name);
    }

    @Override
    protected void save(@NotNull Crate source) {
        source.markDirty();
    }
}
