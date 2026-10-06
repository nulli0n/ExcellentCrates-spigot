package su.nightexpress.excellentcrates.core.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.engine.ui.menu.MenuRegistry;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.ui.dialog.wrap.Dialog;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogRegistry;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class DefaultCoreUIService implements CoreUIService {

    private final MenuRegistry   menus;
    private final DialogRegistry dialogs;

    public DefaultCoreUIService(MenuRegistry menus, DialogRegistry dialogs) {
        this.menus = menus;
        this.dialogs = dialogs;
    }

    @Override
    public <T> void registerDialog(DialogKey<T> key, Dialog<T> dialog) {
        this.dialogs.register(key, dialog);
    }

    @Override
    public <T> void registerMenu(MenuKey<T> key, AbstractObjectMenu<T> menu) {
        this.menus.register(key, menu);
    }

    @Override
    public <T> void unregisterMenu(MenuKey<T> key) {
        this.menus.unregister(key);
    }

    @Override
    public <T> ActionResult openMenu(Player player, MenuKey<T> key, T context) {
        return this.menus.show(player, key, context);
    }

    @Override
    public <T> ActionResult showDialog(Player player, DialogKey<T> key, T context, @Nullable Runnable callback) {
        if (!this.dialogs.show(player, key, context, callback)) {
            return ActionResult.fail(Lang.CORE_UI_ERROR_DIALOG_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, key::id)
            );
        }

        return ActionResult.ok();
    }
}
