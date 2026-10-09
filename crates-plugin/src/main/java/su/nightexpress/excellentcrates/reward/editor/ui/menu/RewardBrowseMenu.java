package su.nightexpress.excellentcrates.reward.editor.ui.menu;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardBrowseMenuContext;
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
public class RewardBrowseMenu extends AbstractObjectMenu<RewardBrowseMenuContext> {

    private final RewardRegistry           repository;
    private final RewardPreviewService     previewService;
    private final RewardEditorUIController controller;

    private final ItemPopulator<Reward> rewardPopulator;

    public RewardBrowseMenu(CratesPlugin plugin,
                            RewardRegistry repository,
                            RewardPreviewService previewService,
                            RewardEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, RewardEditorLang.UI_INVENTORY_BROWSE_TITLE
            .text(), RewardBrowseMenuContext.class);
        this.repository = repository;
        this.previewService = previewService;
        this.controller = controller;

        this.rewardPopulator = ItemPopulator.builder(Reward.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, reward) -> {
                RewardBrowseMenuContext menuContext = this.getObject(context);

                Crate crate = menuContext.crateRef().get();
                if (crate == null) return null;

                return this.previewService.createPreviewIconWithAllPlaceholders(crate, reward)
                    .localized(RewardEditorLang.UI_INVENTORY_BROWSE_BUTTON_REWARD)
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
                    .localized(RewardEditorLang.UI_INVENTORY_BROWSE_BUTTON_CREATION)
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

        RewardBrowseMenuContext menuContext = this.getObject(context);
        boolean quickMode = menuContext.quickMode().get();

        if (!quickMode) {
            event.setCancelled(false);
            return;
        }

        // Always reset click cooldown even if air clicked, so next quick click on item will count as well
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            Crate crate = menuContext.crateRef().get();
            if (crate == null) return;

            Player player = context.getPlayer();
            RewardComponentHook hook = menuContext.hook();

            if (this.controller.onBrowseMenuFastCreateClick(player, hook, crate, new ItemStack(itemStack))) {
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
        RewardBrowseMenuContext menuContext = this.getObject(context);

        boolean quickMode = menuContext.quickMode().get();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.WIND_CHARGE)
                    .hideAllComponents()
                    .localized(RewardEditorLang.UI_INVENTORY_BROWSE_BUTTON_QUICK_MODE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(quickMode))
                    )
                )
                .action(this::handleQuickModeToggle)
                .displayModifier((ctx, item) -> item.setEnchantGlint(quickMode))
                .build()
            )
            .slots(38)
            .build()
        );

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) return;

        List<Reward> rewardIds = crateRewards
            .getRewards()
            .stream()
            .map(entry -> this.repository.resolveReward(crate.id(), entry.getRewardId()))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(reward -> reward.rawId().value()))
            .toList();

        this.rewardPopulator.populateTo(context, rewardIds, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardBrowseMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleQuickModeToggle(ActionContext context) {
        RewardBrowseMenuContext menuContext = this.getObject(context);

        boolean quickMode = menuContext.quickMode().get();
        menuContext.quickMode().set(!quickMode);
        context.getViewer().refresh();
    }

    private void handleCreation(ActionContext context) {
        InventoryClickEvent event = context.getEvent();

        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType().isAir()) return;

        RewardBrowseMenuContext menuContext = this.getObject(context);

        ItemStack copy = new ItemStack(cursor);
        event.getView().setCursor(null);

        Player player = context.getPlayer();
        Players.addItem(player, copy);

        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onBrowseMenuManualCreationClick(player, menuContext, copy, refreshUI);
    }

    private void handleRewardClick(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        RewardBrowseMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        this.controller.onBrowseMenuRewardClick(player, reward, menuContext);
    }
}
