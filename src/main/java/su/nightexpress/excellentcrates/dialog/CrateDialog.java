package su.nightexpress.excellentcrates.dialog;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.nightcore.bridge.dialog.wrap.WrappedDialog;
import su.nightexpress.nightcore.locale.LangContainer;
import su.nightexpress.nightcore.ui.dialog.Dialogs;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

public abstract class CrateDialog<T> implements LangContainer {

    @NotNull
    public abstract WrappedDialog create(@NotNull Player player, @NotNull T source);

    public void show(@NotNull Player player, @NotNull T source, @Nullable Runnable callback) {
        Dialogs.showDialog(player, this.create(player, source), callback);
    }

    @NotNull
    protected static String title(@NotNull String prefix, @NotNull String title) {
        return TagWrappers.YELLOW.and(TagWrappers.BOLD).wrap(prefix.toUpperCase()) + TagWrappers.DARK_GRAY.wrap( " » ") + TagWrappers.WHITE.wrap(title);
    }
}
