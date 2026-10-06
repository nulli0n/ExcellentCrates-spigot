package su.nightexpress.excellentcrates.reward.editor.ui.menu;

import java.util.Comparator;
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
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.BrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.EditorPreferences;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardBrowseMenu extends AbstractObjectMenu<BrowseMenuContext> {

    private final RewardRegistry           repository;
    private final RewardPreviewService     previewService;
    private final RewardEditorUIController controller;

    private final ItemPopulator<Reward> rewardPopulator;

    public RewardBrowseMenu(CratesPlugin plugin,
                            RewardRegistry repository,
                            RewardPreviewService previewService,
                            RewardEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, RewardEditorLang.UI_INVENTORY_REWARDS_TITLE
            .text(), BrowseMenuContext.class);
        this.repository = repository;
        this.previewService = previewService;
        this.controller = controller;

        this.rewardPopulator = ItemPopulator.builder(Reward.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, reward) -> {
                return this.previewService.createPreviewIconWithPlaceholders(reward)
                    .localized(RewardEditorLang.UI_INVENTORY_REWARDS_REWARD)
                    .hideAllComponents();
            })
            .actionProvider(reward -> context -> this.handleRewardClick(context, reward))
            .build();
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addNextPageButton(41);
        this.addPreviousPageButton(39);

        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addBackButton(this::handleBack, 36);

        this.addDefaultButton("create", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .localized(RewardEditorLang.UI_INVENTORY_REWARDS_BUTTON_CREATION)
                )
                .action(this::handleCreation)
                .build()
            )
            .slots(40)
            .build()
        );
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if (slot < inventory.getSize()) return;

        BrowseMenuContext menuContext = this.getObject(context);
        EditorPreferences preferences = menuContext.preferences();

        if (!preferences.isFastMode()) {
            event.setCancelled(false);
            return;
        }

        // Always reset click cooldown even if air clicked, so next quick click on item will count as well
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            Player player = context.getPlayer();

            if (this.controller.onBrowseMenuFastCreateClick(player, new ItemStack(itemStack))) {
                context.getViewer().refresh(); // Refresh on successful creation only.
            }
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
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        List<Reward> rewards = this.repository.values()
            .stream()
            .sorted(Comparator.comparing(Reward::idString))
            .toList();

        this.rewardPopulator.populateTo(context, rewards, items);

        boolean isQuickMode = this.getObject(context).preferences().isFastMode();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BREEZE_ROD)
                    .localized(RewardEditorLang.UI_INVENTORY_REWARDS_BUTTON_QUICK_MODE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(isQuickMode);
                        })
                    )
                )
                .displayModifier((ctx, item) -> {
                    item.setEnchantGlint(isQuickMode);
                })
                .action(this::handleQuickMode)
                .build()
            )
            .slots(38)
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
        BrowseMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleQuickMode(ActionContext context) {
        BrowseMenuContext menuContext = this.getObject(context);

        EditorPreferences preferences = menuContext.preferences();
        preferences.setFastMode(!preferences.isFastMode());
        context.getViewer().refresh();
    }

    private void handleCreation(ActionContext context) {
        InventoryClickEvent event = context.getEvent();

        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType().isAir()) return;

        ItemStack copy = new ItemStack(cursor);
        event.getView().setCursor(null);

        Player player = context.getPlayer();
        Players.addItem(player, copy);

        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onBrowseMenuDetailedCreateClick(player, copy, refreshUI);
    }

    private void handleRewardClick(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        BrowseMenuContext menuContext = this.getObject(context);

        this.controller.onBrowseMenuRewardClick(player, reward, menuContext.backwardNavigator());
    }
}
