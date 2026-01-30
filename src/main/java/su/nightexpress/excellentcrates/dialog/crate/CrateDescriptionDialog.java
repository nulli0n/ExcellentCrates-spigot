package su.nightexpress.excellentcrates.dialog.crate;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.generic.GenericDescriptionDialog;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

import java.util.List;

public class CrateDescriptionDialog extends GenericDescriptionDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Description.Title").text(title("Crate", "Description"));

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
    protected List<String> getDescription(@NotNull Crate source) {
        return source.getDescription();
    }

    @Override
    protected void setDescription(@NotNull Crate source, @NotNull List<String> description) {
        source.setDescription(description);
    }

    @Override
    protected void save(@NotNull Crate source) {
        source.markDirty();
    }
}
