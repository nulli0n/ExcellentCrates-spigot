package su.nightexpress.excellentcrates.preview.crate.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.preview.crate.editor.lang.PreviewComponentLang;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.PreviewComponentEditorUIController;
import su.nightexpress.excellentcrates.preview.crate.editor.ui.menu.context.PreviewComponentEditorMainMenuContext;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class PreviewComponentEditorMainMenu extends AbstractObjectMenu<PreviewComponentEditorMainMenuContext> {

    private final PreviewRegistry                    previewRegistry;
    private final PreviewComponentEditorUIController uiController;

    public PreviewComponentEditorMainMenu(CratesPlugin plugin,
                                          PreviewRegistry previewRegistry,
                                          PreviewComponentEditorUIController uiController) {
        super(plugin, MenuType.GENERIC_9X4, PreviewComponentLang.EDITOR_UI_INVENTORY_COMPONENT_TITLE
            .text(), PreviewComponentEditorMainMenuContext.class);
        this.previewRegistry = previewRegistry;
        this.uiController = uiController;
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
        PreviewComponentEditorMainMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        PreviewComponent component = crate.getComponentOrNull(CrateComponentKeys.PREVIEW);
        if (component == null) return;

        boolean currentState = component.isEnabled();
        AdaptedKey currentKey = component.getPreviewKey();
        Preview openingConfig = this.previewRegistry.getPreviewByKey(currentKey);

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(PreviewComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
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
                    .localized(PreviewComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, true))
                .condition(ctx -> !currentState)
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOW_ITEM_FRAME)
                    .hideAllComponents()
                    .localized(PreviewComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_PREVIEW)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            if (openingConfig == null) {
                                return CoreLang.badEntry(currentKey.asString());
                            }
                            return CoreLang.goodEntry(openingConfig.getName());
                        })
                    )
                )
                .action(ctx -> this.handleComponentPreviewKey(ctx, currentKey))
                .build()
            )
            .slots(14)
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
        PreviewComponentEditorMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleComponentState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        PreviewComponentEditorMainMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateEditorHook hook = menuContext.hook();

        if (this.uiController.onComponentMenuStateClick(player, crate, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleComponentPreviewKey(ActionContext context, AdaptedKey currentKey) {
        Player player = context.getPlayer();
        PreviewComponentEditorMainMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateEditorHook hook = menuContext.hook();

        Runnable refreshUI = () -> this.refresh(player);

        this.uiController.onComponentMenuPreviewClick(player, crate, hook, currentKey, refreshUI);
    }
}
