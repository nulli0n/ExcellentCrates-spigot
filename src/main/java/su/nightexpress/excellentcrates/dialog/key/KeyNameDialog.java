package su.nightexpress.excellentcrates.dialog.key;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.dialog.generic.GenericNameDialog;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.locale.LangEntry;
import su.nightexpress.nightcore.locale.entry.TextLocale;

public class KeyNameDialog extends GenericNameDialog<CrateKey> {

    private static final TextLocale TITLE = LangEntry.builder("Dialog.Key.Name.Title").text(title("Key", "Name"));

    @Override
    @NotNull
    protected TextLocale title() {
        return TITLE;
    }

    @Override
    @NotNull
    protected AdaptedItem getItem(@NotNull CrateKey source) {
        return source.getItem();
    }

    @Override
    protected void setItem(@NotNull CrateKey source, @NotNull AdaptedItem item) {
        source.setItem(item);
    }

    @Override
    @NotNull
    protected String getName(@NotNull CrateKey source) {
        return source.getName();
    }

    @Override
    protected void setName(@NotNull CrateKey source, @NotNull String name) {
        source.setName(name);
    }

    @Override
    protected void save(@NotNull CrateKey source) {
        source.markDirty();
    }
}
