package su.nightexpress.excellentcrates.cost.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostCategoriesMenuContext;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostOptionsMenuContext;

@NullMarked
public final class CostUIKeys {

    public static final MenuKey<CostCategoriesMenuContext> CATEGORIES = MenuKey.of("cost.ui.categories");
    public static final MenuKey<CostOptionsMenuContext>    OPTIONS    = MenuKey.of("cost.ui.options");

    private CostUIKeys() {
    }
}
