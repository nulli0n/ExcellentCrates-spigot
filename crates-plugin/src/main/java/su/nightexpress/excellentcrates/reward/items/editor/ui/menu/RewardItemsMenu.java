package su.nightexpress.excellentcrates.reward.items.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.items.RewardItemsComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.items.editor.ui.RewardItemsEditorUIController;
import su.nightexpress.excellentcrates.reward.items.editor.ui.menu.context.RewardItemsMenuContext;
import su.nightexpress.excellentcrates.reward.items.lang.RewardItemsLang;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardItemsMenu extends AbstractObjectMenu<RewardItemsMenuContext> {

    private static final int[] ITEM_SLOTS = IntStream.range(0, 27).toArray();
    private static final int   ITEM_LIMIT = ITEM_SLOTS.length;

    private final RewardItemsEditorUIController uiController;

    private final ItemPopulator<Integer> itemPopulator;

    public RewardItemsMenu(CratesPlugin plugin,
                           RewardItemsEditorUIController uiController) {
        super(plugin, MenuType.GENERIC_9X4, RewardItemsLang.UI_INVENTORY_ITEMS_TITLE
            .text(), RewardItemsMenuContext.class);
        this.uiController = uiController;

        this.itemPopulator = ItemPopulator.builder(Integer.class)
            .slots(ITEM_SLOTS)
            .itemProvider((context, index) -> {
                RewardItemsMenuContext menuContext = this.getObject(context);

                Reward reward = menuContext.rewardRef().get();
                if (reward == null) return null;

                RewardItemsComponent component = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
                if (component == null) return null;

                List<AdaptedItem> items = component.getItems();
                if (index < 0 || index >= items.size()) return null;

                AdaptedItem itemContent = items.get(index);
                ItemStack itemStack = ItemHelper.toItemStack(itemContent);

                return NightItem.fromItemStack(itemStack);
            })
            .actionProvider(index -> context -> this.handleItemRemove(context, index))
            .build();
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

        RewardItemsMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardEditorHook hook = menuContext.hook();

        // Always reset click cooldown even if air clicked, so next quick click on item will count as well
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            Player player = context.getPlayer();
            Runnable refreshUI = () -> this.refresh(player);

            this.uiController.onItemsItemClick(player, reward, hook, new ItemStack(itemStack), refreshUI);
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
        RewardItemsMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardItemsComponent content = reward.getComponentOrNull(RewardComponentKeys.ITEMS);
        if (content == null) return;

        int itemCount = content.getItems().size();
        boolean isFull = itemCount >= ITEM_LIMIT;

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .with(SharedPlaceholders.CURRENT, () -> String.valueOf(itemCount))
            .with(SharedPlaceholders.MAX, () -> String.valueOf(ITEM_LIMIT))
            .build();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_SHULKER_BOX)
                    .hideAllComponents()
                    .localized(RewardItemsLang.UI_INVENTORY_ITEMS_ADD_AVAILABLE)
                    .setPlaceholderContext(placeholders)
                )
                .condition(ctx -> !isFull)
                .build()
            )
            .state("full", ItemState.builder()
                .icon(NightItem.fromType(Material.RED_SHULKER_BOX)
                    .hideAllComponents()
                    .localized(RewardItemsLang.UI_INVENTORY_ITEMS_ADD_UNAVAILABLE)
                    .setPlaceholderContext(placeholders)
                )
                .condition(ctx -> isFull)
                .build()
            )
            .slots(31)
            .build()
        );

        List<Integer> itemIndices = IntStream.range(0, itemCount).boxed().toList();
        this.itemPopulator.populateTo(context, itemIndices, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardItemsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleItemRemove(ActionContext context, int index) {
        InventoryClickEvent clickEvent = context.getEvent();
        if (clickEvent.getClick() != ClickType.DROP) return;

        Player player = context.getPlayer();
        RewardItemsMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardEditorHook hook = menuContext.hook();

        this.uiController.onItemsItemRemoveClick(player, reward, hook, index, () -> {
            this.refresh(player);
        });
    }
}
