package su.nightexpress.excellentcrates.cost.ui.controller;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.cost.ui.CostUIKeys;
import su.nightexpress.excellentcrates.cost.ui.menu.CostCategoriesMenu;
import su.nightexpress.excellentcrates.cost.ui.menu.CostOptionsMenu;

@NullMarked
public final class CostUIMenuRegistrar implements PluginComponent {

    private final Path                              menuDir;
    private final CratesPlugin                      plugin;
    private final CoreUIService                     coreUI;
    private final CrateResolver                     crateResolver;
    private final IdentifiableRegistry<CostType<?>> costTypes;

    public CostUIMenuRegistrar(Path menuDir,
                               CratesPlugin plugin,
                               CoreUIService coreUI,
                               CrateResolver crateResolver,
                               IdentifiableRegistry<CostType<?>> costTypes) {
        this.menuDir = menuDir;
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateResolver = crateResolver;
        this.costTypes = costTypes;
    }

    @Override
    public void start() {
        this.loadMenus();
    }

    @Override
    public void reload() {
        this.shutdown();
        this.loadMenus();
    }

    @Override
    public void shutdown() {
        this.coreUI.unregisterMenu(CostUIKeys.CATEGORIES);
        this.coreUI.unregisterMenu(CostUIKeys.OPTIONS);
    }

    public boolean isRunning() {
        return true;
    }

    private void loadMenus() {
        CostCategoriesMenu categoriesMenu = new CostCategoriesMenu(plugin, crateResolver, costTypes);
        CostOptionsMenu optionsMenu = new CostOptionsMenu(plugin, crateResolver, costTypes);

        categoriesMenu.load(this.menuDir.resolve("crate_cost_categories.yml"));
        optionsMenu.load(this.menuDir.resolve("crate_cost_choice.yml"));

        this.coreUI.registerMenu(CostUIKeys.CATEGORIES, categoriesMenu);
        this.coreUI.registerMenu(CostUIKeys.OPTIONS, optionsMenu);
    }
}
