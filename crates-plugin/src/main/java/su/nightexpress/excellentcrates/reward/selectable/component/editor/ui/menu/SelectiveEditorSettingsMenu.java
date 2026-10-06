package su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.SelectiveEditorUIController;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu.context.SelectiveEditorSettingsMenuContext;
import su.nightexpress.excellentcrates.reward.selectable.lang.SelectableLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class SelectiveEditorSettingsMenu extends AbstractObjectMenu<SelectiveEditorSettingsMenuContext> {

    private final SelectiveEditorUIController controller;

    public SelectiveEditorSettingsMenu(CratesPlugin plugin,
                                       SelectiveEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, SelectableLang.EDITOR_UI_INVENTORY_SETTINGS_TITLE
            .text(), SelectiveEditorSettingsMenuContext.class);
        this.controller = controller;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 27);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {

    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        SelectiveEditorSettingsMenuContext menuContext = this.getObject(context);

        CrateReference crateRef = menuContext.crateRef();
        Crate crate = crateRef.get();
        if (crate == null) return;

        SelectableRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.SELECTABLE_REWARDS);
        if (component == null) return;

        boolean currentState = component.isEnabled();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(SelectableLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, false))
                .condition(ctx -> currentState)
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(SelectableLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, true))
                .condition(ctx -> !currentState)
                .build()
            )
            .slots(13)
            .build()
        );
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        SelectiveEditorSettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleComponentState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        SelectiveEditorSettingsMenuContext menuContext = this.getObject(context);
        CrateEditorHook hook = menuContext.hook();
        CrateReference crateRef = menuContext.crateRef();
        Crate crate = crateRef.get();
        if (crate == null) return;

        if (this.controller.onComponentMenuStateClick(player, crate, hook, newState)) {
            this.refresh(player);
        }
    }
}
