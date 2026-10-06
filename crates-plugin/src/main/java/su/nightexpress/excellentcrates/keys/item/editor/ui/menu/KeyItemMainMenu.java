package su.nightexpress.excellentcrates.keys.item.editor.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyItem;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIController;
import su.nightexpress.excellentcrates.keys.item.editor.ui.menu.context.KeyItemMainMenuContext;
import su.nightexpress.excellentcrates.keys.item.lang.KeyItemLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyItemMainMenu extends AbstractObjectMenu<KeyItemMainMenuContext> {

    private final KeyResolver               resolver;
    private final KeyItemFactory            itemFactory;
    private final KeyItemEditorUIController controller;

    public KeyItemMainMenu(CratesPlugin plugin,
                           KeyResolver resolver,
                           KeyItemFactory itemFactory,
                           KeyItemEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, KeyItemLang.UI_INVENTORY_ITEM_TITLE.text(), KeyItemMainMenuContext.class);
        this.resolver = resolver;
        this.itemFactory = itemFactory;
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
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if (slot < inventory.getSize()) return;

        // Reset click cooldown to allow fast double clicks.
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            this.handleItem(context, new ItemStack(itemStack));
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
        KeyItemMainMenuContext menuContext = this.getObject(context);
        CrateKey key = this.resolver.resolveKey(menuContext.keyId());
        if (key == null) return;

        KeyItem crateItem = key.getItem();
        boolean stackable = crateItem.isStackable();
        boolean inheritDisplay = crateItem.isInheritDisplaySettings();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(this.itemFactory.createDisplayIcon(key)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_BUTTON_ICON)
                )
                .condition(viewContext -> this.itemFactory.hasValidItem(key))
                .action(actionContext -> {
                    // Ensure the item it still valid after a while the GUI was opened.
                    this.itemFactory.createBaseItem(key).ifPresent(itemStack -> {
                        this.handleItemReceive(actionContext, itemStack);
                    });
                })
                .build()
            )
            // Draw explicit "error" button instead of just "fallback" item provided by the Render Service,
            // so user can know there is actually something wrong.
            .state("invalid", ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_BUTTON_ICON_INVALID)
                )
                .condition(viewContext -> !this.itemFactory.hasValidItem(key))
                .build()
            )
            .slots(11)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.TRIAL_KEY)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_BUTTON_STACKABLE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(false);
                        })
                    )
                )
                .condition(viewContext -> !stackable)
                .action(actionContext -> this.handleStackable(actionContext, true))
                .build()
            )
            .state("enabled", ItemState.builder()
                .icon(NightItem.fromType(Material.TRIAL_KEY)
                    .setAmount(16)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_BUTTON_STACKABLE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(true);
                        })
                    )
                )
                .condition(viewContext -> stackable)
                .action(actionContext -> this.handleStackable(actionContext, false))
                .build()
            )
            .slots(13)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ITEM_FRAME)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_INHERIT_DISPLAY_SETTINGS)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(false);
                        })
                    )
                )
                .condition(viewContext -> !inheritDisplay)
                .action(ctx -> this.handleInheritDisplay(ctx, true))
                .build()
            )
            .state("enabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GLOW_ITEM_FRAME)
                    .hideAllComponents()
                    .localized(KeyItemLang.UI_INVENTORY_ITEM_INHERIT_DISPLAY_SETTINGS)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(true);
                        })
                    )
                )
                .condition(viewContext -> inheritDisplay)
                .action(ctx -> this.handleInheritDisplay(ctx, false))
                .build()
            )
            .slots(15)
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
        KeyItemMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleItemReceive(ActionContext context, ItemStack itemStack) {
        Player player = context.getPlayer();
        Players.addItem(player, itemStack);
    }

    private void handleItem(ViewerContext context, ItemStack itemStack) {
        Player player = context.getPlayer();
        KeyItemMainMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onMainMenuIconClick(player, menuContext, itemStack, refreshUI);
    }

    private void handleStackable(ActionContext context, boolean state) {
        Player player = context.getPlayer();
        KeyItemMainMenuContext menuContext = this.getObject(context);

        this.controller.onMainMenuStackableClick(player, menuContext, state);
        context.getViewer().refresh();
    }

    private void handleInheritDisplay(ActionContext context, boolean state) {
        Player player = context.getPlayer();
        KeyItemMainMenuContext menuContext = this.getObject(context);
        KeyEditorHook hook = menuContext.hook();

        this.controller.onSettingsInheritDisplayClick(player, hook, state);
        context.getViewer().refresh();
    }
}
