package su.nightexpress.excellentcrates.core.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.engine.ui.menu.MenuRegistry;
import su.nightexpress.excellentcrates.core.lang.Lang;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class DefaultMenuRegistry implements MenuRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMenuRegistry.class);

    private final Map<MenuKey<?>, AbstractObjectMenu<?>> byKey;

    public DefaultMenuRegistry() {
        this.byKey = new HashMap<>();
    }

    public void clear() {
        this.byKey.clear();
    }

    public <T> void register(MenuKey<T> key, Supplier<AbstractObjectMenu<T>> supplier) {
        this.register(key, supplier.get());
    }

    public <T> void register(MenuKey<T> key, AbstractObjectMenu<T> menu) {
        this.byKey.putIfAbsent(key, menu);
    }

    public <T> void unregister(MenuKey<T> key) {
        AbstractObjectMenu<?> menu = this.byKey.remove(key);
        if (menu != null) {
            menu.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <T> ActionResult show(Player player, MenuKey<T> key, T context) {
        AbstractObjectMenu<T> menu = (AbstractObjectMenu<T>) this.byKey.get(key);

        if (menu == null) {
            LOGGER.warn("Menu '{}' not found or disabled.", key.id());
            return ActionResult.fail(Lang.CORE_UI_ERROR_MENU_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, () -> key.id().value())
            );
        }

        return ActionResult.of(menu.show(player, context));
    }
}
