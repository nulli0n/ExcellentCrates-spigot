package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu;

import java.util.Comparator;
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

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsAlternativeMenuContext;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardLimitsAlternativeMenu extends AbstractObjectMenu<RewardLimitsAlternativeMenuContext> {

    private final RewardRegistry                 registry;
    private final RewardPreviewService           previewService;
    private final RewardPlaceholders             rewardPlaceholders;
    private final RewardLimitsEditorUIController controller;

    private final ItemPopulator<Reward> rewardPopulator;

    public RewardLimitsAlternativeMenu(CratesPlugin plugin,
                                       RewardRegistry registry,
                                       RewardPreviewService previewService,
                                       RewardPlaceholders rewardPlaceholders,
                                       RewardLimitsEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, RewardLimitsLang.EDITOR_UI_INVENTORY_ALTERNATIVE_REWARD_TITLE
            .text(), RewardLimitsAlternativeMenuContext.class);
        this.registry = registry;
        this.previewService = previewService;
        this.rewardPlaceholders = rewardPlaceholders;
        this.controller = controller;

        this.rewardPopulator = ItemPopulator.builder(Reward.class)
            .itemProvider((context, rewardId) -> this.createRewardItem(rewardId))
            .actionProvider(rewardId -> context -> this.handleRewardClick(context, rewardId))
            .slots(IntStream.range(0, 36).toArray())
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

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
        List<Reward> rewardIds = this.registry.values()
            .stream()
            .sorted(Comparator.comparing(r -> r.rawId().value()))
            .toList();

        this.rewardPopulator.populateTo(context, rewardIds, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private @Nullable NightItem createRewardItem(Reward reward) {
        NightItem icon = this.previewService.createPreviewIcon(reward);

        return icon
            .hideAllComponents()
            .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_ALTERNATIVE_REWARD_BUTTON_REWARD)
            .replace(ctx -> ctx
                .apply(this.rewardPlaceholders.basePlaceholders(reward))
            );
    }

    private void handleBack(ActionContext context) {
        RewardLimitsAlternativeMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleRewardClick(ActionContext context, Reward reward) {
        Player player = context.getPlayer();
        RewardLimitsAlternativeMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();

        if (this.controller.onAlternativeMenuRewardClick(player, reward.id(), hook)) {
            menuContext.moveBackward(player);
        }
    }
}
