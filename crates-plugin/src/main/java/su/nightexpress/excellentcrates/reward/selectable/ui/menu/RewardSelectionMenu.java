package su.nightexpress.excellentcrates.reward.selectable.ui.menu;

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
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.text.layout.TextLayout;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickContext;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickService;
import su.nightexpress.excellentcrates.reward.selectable.ui.SelectiveUIController;
import su.nightexpress.excellentcrates.reward.selectable.ui.menu.context.RewardSelectionMenuContext;
import su.nightexpress.excellentcrates.reward.selectable.ui.settings.SelectiveUISettings;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RewardSelectionMenu extends AbstractObjectMenu<RewardSelectionMenuContext> {

    private static final String DEFAULT_TITLE = "Select Rewards: %s/%s"
        .formatted(SharedPlaceholders.CURRENT, SharedPlaceholders.MAX);

    private final RewardPreviewService  previewService;
    private final RewardPlaceholders    placeholders;
    private final SelectivePickService  pickService;
    private final SelectiveUIController uiController;

    private SelectiveUISettings settings;

    public RewardSelectionMenu(CratesPlugin plugin,
                               RewardPreviewService previewService,
                               RewardPlaceholders placeholders,
                               SelectivePickService pickService,
                               SelectiveUIController uiController) {
        super(plugin, MenuType.GENERIC_9X6, DEFAULT_TITLE, RewardSelectionMenuContext.class);
        this.previewService = previewService;
        this.placeholders = placeholders;
        this.pickService = pickService;
        this.uiController = uiController;

        this.settings = SelectiveUISettings.defaults();
    }

    @Override
    protected String getRawTitle(ViewerContext context) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        int requiredRewards = menuContext.pickContext().requiredRewards();
        int selectedRewards = menuContext.pickContext().selectedRewards().size();

        return super.getRawTitle(context)
            .replace(SharedPlaceholders.CURRENT, String.valueOf(selectedRewards))
            .replace(SharedPlaceholders.MAX, String.valueOf(requiredRewards));
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addDefaultButton("confirm", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_STAINED_GLASS_PANE)
                    .hideAllComponents()
                    .setDisplayName(TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("Confirm"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Click to confirm your selection.")
                    ))
                )
                .condition(this::canConfirm)
                .action(this::onConfirmClick)
                .build()
            )
            .state("not_ready", ItemState.builder()
                .icon(NightItem.fromType(Material.RED_STAINED_GLASS_PANE)
                    .hideAllComponents()
                    .setDisplayName(TagWrappers.RED.and(TagWrappers.BOLD).wrap("Not Ready"))
                    .setLore(Lists.newList(
                        TagWrappers.GRAY.wrap("Please select the required number of rewards.")
                    ))
                )
                .condition(ctx -> !this.canConfirm(ctx))
                .action(ctx -> {
                })
                .build()
            )
            .slots(49)
            .build()
        );
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        menuContext.onAbort().run();
    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {
        this.settings = SelectiveUISettings.loadFrom(config);
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        CrateReference crateReference = menuContext.crateReference();

        Crate crate = crateReference.get();
        if (crate == null) {
            return;
        }

        SelectivePickContext pickContext = menuContext.pickContext();

        CrateRewardsComponent rewardsComponent = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (rewardsComponent == null) {
            return;
        }

        Player player = context.getPlayer();
        List<Reward> availableRewards = this.pickService.getAvailableRewards(player, crate, pickContext);

        ItemPopulator<Reward> rewardPopulator = ItemPopulator.builder(Reward.class)
            .actionProvider(reward -> ctx -> this.onRewardPickClick(ctx, reward))
            .itemProvider((ctx, reward) -> this.getRewardIcon(context, reward, this.settings.rewardPickLore()))
            .slots(this.settings.rewardSlots())
            .build();

        rewardPopulator.populateTo(context, availableRewards, items);

        List<RewardReference> selectedRewards = pickContext.selectedRewards();
        int[] selectedSlots = this.settings.selectedSlots().getSlots(selectedRewards.size());
        for (int index = 0; index < selectedRewards.size(); index++) {
            if (index >= selectedSlots.length) {
                break;
            }

            int slot = selectedSlots[index];
            RewardReference selectedRef = selectedRewards.get(index);
            Reward reward = selectedRef.get();
            if (reward == null) {
                continue;
            }

            NightItem rewardIcon = this.getRewardIcon(context, reward, this.settings.rewardUnpickLore());
            if (rewardIcon == null) {
                continue;
            }

            MenuItem rewardItem = MenuItem.custom()
                .defaultState(ItemState.builder()
                    .icon(rewardIcon)
                    .action(ctx -> this.onRewardUnpickClick(ctx, reward))
                    .build()
                )
                .slots(slot)
                .build();

            items.add(rewardItem);
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private @Nullable NightItem getRewardIcon(ViewerContext context, Reward reward, TextLayout rewardLore) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        CrateReference crateReference = menuContext.crateReference();
        Crate crate = crateReference.get();
        if (crate == null) {
            return null;
        }

        Player player = context.getPlayer();

        PlaceholderContext placeholders = PlaceholderContext.builder()
            .apply(this.placeholders.allPlaceholders(crate, reward, player))
            .build();

        String name = placeholders.apply(this.settings.rewardName());
        List<String> lore = rewardLore.render(placeholders);

        NightItem icon = this.previewService.createPreviewIcon(reward);

        return icon
            .setDisplayName(name)
            .setLore(lore);
    }

    private boolean canConfirm(ViewerContext context) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        SelectivePickContext pickContext = menuContext.pickContext();

        CrateReference crateReference = menuContext.crateReference();
        Crate crate = crateReference.get();
        if (crate == null) {
            return false;
        }

        Player player = context.getPlayer();
        return this.pickService.validatePick(player, crate, pickContext).success();
    }

    private void onRewardPickClick(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        RewardSelectionMenuContext menuContext = this.getObject(context);
        SelectivePickContext pickContext = menuContext.pickContext();

        CrateReference crateReference = menuContext.crateReference();
        Crate crate = crateReference.get();
        if (crate == null) {
            this.close(player);
            return;
        }

        if (this.uiController.onSelectionRewardPick(player, crate, reward, pickContext)) {
            this.settings.pickSound().play(player);
            this.refresh(player);
        }
    }

    private void onRewardUnpickClick(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        RewardSelectionMenuContext menuContext = this.getObject(context);
        SelectivePickContext pickContext = menuContext.pickContext();

        CrateReference crateReference = menuContext.crateReference();
        Crate crate = crateReference.get();
        if (crate == null) {
            this.close(player);
            return;
        }

        if (this.uiController.onSelectionRewardUnpick(player, crate, reward, pickContext)) {
            this.settings.unpickSound().play(player);
            this.refresh(player);
        }
    }

    private void onConfirmClick(ActionContext context) {
        RewardSelectionMenuContext menuContext = this.getObject(context);
        SelectivePickContext pickContext = menuContext.pickContext();

        menuContext.onComplete().accept(pickContext);
    }
}
