package su.nightexpress.engine.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;

@NullMarked
public interface CoreUIService {

    <T> void registerDialog(DialogKey<T> key, Dialog<T> dialog);

    <T> void registerMenu(MenuKey<T> key, AbstractObjectMenu<T> menu);

    <T> void unregisterMenu(MenuKey<T> key);

    <T> ActionResult openMenu(Player player, MenuKey<T> key, T context);

    <T> ActionResult showDialog(Player player, DialogKey<T> key, T context, @Nullable Runnable callback);
}
