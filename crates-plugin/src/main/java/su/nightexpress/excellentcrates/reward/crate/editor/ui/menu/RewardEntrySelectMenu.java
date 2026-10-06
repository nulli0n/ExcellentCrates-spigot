package su.nightexpress.excellentcrates.reward.crate.editor.ui.menu;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context.RewardEntrySelectMenuContext;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;

@NullMarked
public class RewardEntrySelectMenu extends AbstractObjectMenu<RewardEntrySelectMenuContext> {

    private final RewardResolver       rewardResolver;
    private final RewardPreviewService renderService;

    private final ItemPopulator<Reward> rewardPopulator;

    public RewardEntrySelectMenu(CratesPlugin plugin,
                                 RewardResolver rewardResolver,
                                 RewardPreviewService renderService) {
        super(plugin, MenuType.GENERIC_9X5, RewardComponentLang.EDITOR_UI_INVENTORY_SELECTION_TITLE
            .text(), RewardEntrySelectMenuContext.class);
        this.rewardResolver = rewardResolver;
        this.renderService = renderService;

        this.rewardPopulator = ItemPopulator.builder(Reward.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, reward) -> {
                return this.renderService.createPreviewIconWithPlaceholders(reward)
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_SELECTION_BUTTON_REWARD)
                    .hideAllComponents();
            })
            .actionProvider(reward -> context -> this.handleRewardClick(context, reward.id()))
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addNextPageButton(41);
        this.addPreviousPageButton(39);
        this.addBackButton(this::handleBack, 36);
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
        RewardEntrySelectMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        RewardReference rewardRef = menuContext.rewardRef();
        Reward reward = rewardRef == null ? null : rewardRef.get();

        CrateRewardsComponent rewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (rewards == null) return;

        Predicate<Reward> skipReward = target -> {
            if (rewards.hasReward(target.id())) return false;

            return reward == null || !target.id().equals(reward.id());
        };

        List<Reward> rewardsList = this.rewardResolver.rewards()
            .stream()
            .filter(skipReward)
            .sorted(Comparator.comparing(Reward::idString))
            .toList();

        this.rewardPopulator.populateTo(context, rewardsList, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardEntrySelectMenuContext selectionContext = this.getObject(context);

        selectionContext.moveBackward(context.getPlayer());
    }

    private void handleRewardClick(ActionContext context, Identifier selectedId) {
        RewardEntrySelectMenuContext selectionContext = this.getObject(context);

        selectionContext.callback().accept(selectedId);
    }
}
