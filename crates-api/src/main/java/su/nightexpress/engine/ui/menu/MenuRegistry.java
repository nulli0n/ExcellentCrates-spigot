package su.nightexpress.engine.ui.menu;

import java.util.function.Supplier;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;

@NullMarked
public interface MenuRegistry {

    void clear();

    <T> void register(MenuKey<T> key, Supplier<AbstractObjectMenu<T>> supplier);

    <T> void register(MenuKey<T> key, AbstractObjectMenu<T> menu);

    <T> void unregister(MenuKey<T> key);

    <T> ActionResult show(Player player, MenuKey<T> key, T context);
}
