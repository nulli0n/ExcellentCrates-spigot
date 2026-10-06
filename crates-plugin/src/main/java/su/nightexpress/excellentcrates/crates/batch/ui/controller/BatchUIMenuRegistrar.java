package su.nightexpress.excellentcrates.crates.batch.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.crates.batch.ui.BatchUIKeys;
import su.nightexpress.excellentcrates.crates.batch.ui.menu.BatchAmountSelectionMenu;

@NullMarked
public class BatchUIMenuRegistrar extends BasePluginComponent {

    private final CratesPlugin  plugin;
    private final CoreUIService coreUI;

    public BatchUIMenuRegistrar(CratesPlugin plugin,
                                CoreUIService coreUI) {
        super();
        this.plugin = plugin;
        this.coreUI = coreUI;
    }

    @Override
    protected void onReload() {
        this.shutdown();
        this.start();
    }

    @Override
    protected void onShutdown() {
        this.coreUI.unregisterMenu(BatchUIKeys.AMOUNT_SELECTION);
    }

    @Override
    protected void onStart() {
        this.registerMenus();
    }

    private void registerMenus() {
        BatchAmountSelectionMenu selectionMenu = new BatchAmountSelectionMenu(this.plugin);

        selectionMenu.load(plugin.menuPath().resolve("batch_amount_selection_menu.yml"));

        this.coreUI.registerMenu(BatchUIKeys.AMOUNT_SELECTION, selectionMenu);
    }
}
