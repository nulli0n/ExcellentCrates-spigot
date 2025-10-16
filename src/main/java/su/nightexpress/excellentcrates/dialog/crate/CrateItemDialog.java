package su.nightexpress.excellentcrates.dialog.crate;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.dialog.generic.GenericItemDialog;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

import java.util.List;

public class CrateItemDialog extends GenericItemDialog<Crate> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Crate.Item.Title").text(title("Crate", "Item"));

    @Override
    @NotNull
    protected TextLocale title() {
        return TITLE;
    }

    @Override
    protected void setName(@NotNull Crate source, @NotNull String name) {
        source.setName(name);
    }

    @Override
    protected void setDescription(@NotNull Crate source, @NotNull List<String> description) {
        source.setDescription(description);
    }

    @Override
    protected void setItem(@NotNull Crate source, @NotNull AdaptedItem item) {
        source.setItem(item);
        source.markDirty();
    }
}
