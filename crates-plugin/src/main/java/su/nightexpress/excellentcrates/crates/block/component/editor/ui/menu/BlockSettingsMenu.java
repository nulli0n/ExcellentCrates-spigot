package su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.block.crate.BlockComponent;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context.BlockSettingsMenuContext;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class BlockSettingsMenu extends AbstractObjectMenu<BlockSettingsMenuContext> {

    private final CrateResolver           crateResolver;
    private final BlockEditorUIController controller;

    public BlockSettingsMenu(CratesPlugin plugin,
                             CrateResolver crateResolver,
                             BlockEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, BlocksLang.EDITOR_UI_INVENTORY_COMPONENT_TITLE
            .text(), BlockSettingsMenuContext.class);
        this.crateResolver = crateResolver;
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
        int slot = event.getRawSlot();
        Inventory inventory = event.getInventory();
        if (slot >= inventory.getSize()) {
            event.setCancelled(false);
        }
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
        BlockSettingsMenuContext menuContext = this.getObject(context);
        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) {
            return;
        }

        BlockComponent component = crate.getComponentOrNull(CrateComponentKeys.BLOCK);

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_CHEST)
                    .localized(BlocksLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_ASSIGN)
                    .hideAllComponents()
                )
                .action(this::handleAssign)
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .localized(BlocksLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_UNLINK)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_AMOUNT, () -> {
                            if (component == null) {
                                return String.valueOf(0);
                            }
                            return NumberUtil.format(component.countAllBlockPositions());
                        })
                    )
                )
                .action(this::handleUnlink)
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
        BlockSettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleAssign(ActionContext context) {
        Player player = context.getPlayer();
        BlockSettingsMenuContext menuContext = this.getObject(context);
        InventoryClickEvent event = context.getEvent();
        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType().isAir()) {
            return;
        }

        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) {
            return;
        }

        this.controller.onSettingsMenuAssignClick(player, crate, cursor, assigned -> {
            InventoryView currentView = context.getViewer().getCurrentView();
            if (currentView != null) {
                currentView.setCursor(assigned);
            }
        });
    }

    private void handleUnlink(ActionContext context) {
        Player player = context.getPlayer();
        BlockSettingsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onComponentMenuUnlinkClick(player, menuContext, refreshUI);
    }
}
