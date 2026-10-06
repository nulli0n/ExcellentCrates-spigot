package su.nightexpress.excellentcrates.reward.crate.editor.ui.menu;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryBrowseMenuContext;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardEntryBrowseMenu extends AbstractObjectMenu<RewardEntryBrowseMenuContext> {

    private final RewardResolver                    rewardResolver;
    private final RewardPreviewService              previewService;
    private final RewardComponentEditorUIController controller;

    private final ItemPopulator<Reward> rewardPopulator;

    public RewardEntryBrowseMenu(CratesPlugin plugin,
                                 RewardResolver rewardResolver,
                                 RewardPreviewService previewService,
                                 RewardComponentEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, RewardComponentLang.EDITOR_UI_INVENTORY_BROWSE_TITLE
            .text(), RewardEntryBrowseMenuContext.class);
        this.rewardResolver = rewardResolver;
        this.controller = controller;
        this.previewService = previewService;

        this.rewardPopulator = ItemPopulator.builder(Reward.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, reward) -> {
                RewardEntryBrowseMenuContext menuContext = this.getObject(context);

                Crate crate = menuContext.crateRef().get();
                if (crate == null) return null;

                return this.previewService.createPreviewIconWithAllPlaceholders(crate, reward)
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_BROWSE_BUTTON_REWARD)
                    .hideAllComponents();
            })
            .actionProvider(reward -> context -> this.handleReward(context, reward))
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
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addNextPageButton(41);
        this.addPreviousPageButton(39);
        this.addBackButton(this::handleBack, 36);

        this.addDefaultButton("reward_add", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.SPYGLASS)
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_BROWSE_BUTTON_ADD)
                )
                .action(this::handleAdd)
                .build()
            )
            .slots(40)
            .build()
        );

        this.addDefaultButton("reward_editor", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.CRAFTING_TABLE)
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_BROWSE_BUTTON_EDITOR)
                )
                .action(this::handleEditor)
                .build()
            )
            .slots(42)
            .build()
        );
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
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateRewardsComponent rewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (rewards == null) return;

        int requiredAmount = rewards.getRequiredAmount();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.DISPENSER)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_BROWSE_BUTTON_REQUIRED_AMOUNT)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> NumberUtil.format(requiredAmount))
                    )
                )
                .action(ctx -> this.handleRequiredAmount(ctx, requiredAmount))
                .build()
            )
            .slots(38)
            .build()
        );

        List<Reward> rewardIds = rewards
            .getRewards()
            .stream()
            .map(entry -> this.rewardResolver.resolveReward(entry.getRewardId()))
            .filter(Objects::nonNull)
            .sorted(Comparator.comparing(Reward::idString))
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
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleRequiredAmount(ActionContext context, int currentAmount) {
        Player player = context.getPlayer();
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onBrowseMenuRequiredAmountClick(player, menuContext, currentAmount, refreshUI);
    }

    private void handleAdd(ActionContext context) {
        Player player = context.getPlayer();
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onBrowseMenuAddClick(player, menuContext, refreshUI);
    }

    private void handleEditor(ActionContext context) {
        Player player = context.getPlayer();
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);

        this.controller.onBrowseMenuEditorClick(player, menuContext);
    }

    private void handleReward(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        RewardEntryBrowseMenuContext menuContext = this.getObject(context);

        this.controller.onBrowseMenuRewardClick(player, menuContext, reward);
    }

}
