package su.nightexpress.excellentcrates.reward.crate.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntryOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardEntryOptionsMenu extends AbstractObjectMenu<RewardEntryOptionsMenuContext> {

    private final RewardPreviewService              previewService;
    private final RewardPlaceholders                rewardPlaceholders;
    private final RewardComponentEditorUIController controller;

    public RewardEntryOptionsMenu(CratesPlugin plugin,
                                  RewardPreviewService previewService,
                                  RewardPlaceholders rewardPlaceholders,
                                  RewardComponentEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardComponentLang.EDITOR_UI_INVENTORY_OPTIONS_TITLE
            .text(), RewardEntryOptionsMenuContext.class);
        this.previewService = previewService;
        this.rewardPlaceholders = rewardPlaceholders;
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
        RewardEntryOptionsMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        CrateRewardsComponent rewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (rewards == null) return;

        CrateRewardEntry rewardEntry = rewards.getReward(reward.id());
        if (rewardEntry == null) return;

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(this.previewService.createPreviewIcon(reward)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_OPTIONS_BUTTON_ID)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> rewardEntry.getRewardId().value())
                    )
                )
                .action(this::handleRewardId)
                .build()
            )
            .slots(10)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_OPTIONS_BUTTON_WEIGHT)
                    .replace(ctx -> ctx
                        .apply(this.rewardPlaceholders.allPlaceholders(crate, reward))
                    )
                )
                .action(actionContext -> this.handleWeight(actionContext, rewardEntry.getWeight()))
                .build()
            )
            .slots(13)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.CRAFTING_TABLE)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_OPTIONS_BUTTON_EDITOR)
                )
                .action(this::handleRewardEditor)
                .build()
            )
            .slots(16)
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
        RewardEntryOptionsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleRewardId(ActionContext context) {
        Player player = context.getPlayer();
        RewardEntryOptionsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onOptionsIdClick(player, menuContext, refreshUI);
    }

    private void handleWeight(ActionContext context, double currentWeight) {
        Player player = context.getPlayer();
        RewardEntryOptionsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onOptionsWeightClick(player, menuContext, currentWeight, refreshUI);
    }

    private void handleRewardEditor(ActionContext context) {
        Player player = context.getPlayer();
        RewardEntryOptionsMenuContext menuContext = this.getObject(context);

        this.controller.onOptionsEditorClick(player, menuContext);
    }
}
