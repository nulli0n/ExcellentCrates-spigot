package su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu;

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

import su.nightexpress.excellentcrates.api.common.limit.LimitSnapshot;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.RewardLimitsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.limit.editor.ui.menu.context.RewardLimitsMainMenuContext;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardLimitsMainMenu extends AbstractObjectMenu<RewardLimitsMainMenuContext> {

    private final RewardResolver                 resolver;
    private final RewardPreviewService           previewService;
    private final RewardLimitsEditorUIController controller;

    public RewardLimitsMainMenu(NightCorePlugin plugin,
                                RewardResolver resolver,
                                RewardPreviewService previewService,
                                RewardLimitsEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_TITLE
            .text(), RewardLimitsMainMenuContext.class);
        this.resolver = resolver;
        this.previewService = previewService;
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
        RewardLimitsMainMenuContext menuContext = this.getObject(context);
        RewardId rewardId = menuContext.rewardId();

        Reward reward = this.resolver.resolveReward(rewardId);
        if (reward == null) return;

        RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        if (limit == null) return;

        for (LimitType type : LimitType.values()) {
            LimitSnapshot snapshot = LimitSnapshot.of(switch (type) {
                case GLOBAL -> limit.getGlobalOptions();
                case INDIVIDUAL -> limit.getIndividualOptions();
            });

            items.add(this.createLimitButton(type, snapshot));
        }

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleState(ctx, false))
                .condition(ctx -> limit.isEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleState(ctx, true))
                .condition(ctx -> !limit.isEnabled())
                .build()
            )
            .slots(10)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_SHULKER_BOX)
                    .hideAllComponents()
                    .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_ALTERNATIVE_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleAlternativeState(ctx, false))
                .condition(ctx -> limit.isAlternativeEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.RED_SHULKER_BOX)
                    .hideAllComponents()
                    .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_ALTERNATIVE_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleAlternativeState(ctx, true))
                .condition(ctx -> !limit.isAlternativeEnabled())
                .build()
            )
            .slots(14)
            .build()
        );

        items.add(this.createAlternativeRewardButton(limit));
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private MenuItem createAlternativeRewardButton(RewardLimitComponent limit) {
        RewardId rewardId = limit.getAlternativeRewardId();
        Reward reward = this.resolver.resolveReward(rewardId);

        NightItem icon;

        if (reward == null) {
            icon = NightItem.fromType(Material.BARRIER)
                .replace(ctx -> ctx
                    .with(CommonPlaceholders.GENERIC_VALUE, () -> CoreLang.badEntry(rewardId.rewardId().value()))
                );
        }
        else {
            icon = this.previewService.createPreviewIconWithPlaceholders(reward);
        }

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(icon
                    .hideAllComponents()
                    .localized(RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_ALTERNATIVE_REWARD_ID)
                )
                .action(ctx -> this.handleAlternativeRewardId(ctx))
                .build()
            )
            .slots(15)
            .build();
    }

    private MenuItem createLimitButton(LimitType type, LimitSnapshot snapshot) {
        IconLocale locale = switch (type) {
            case GLOBAL -> RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_GLOBAL;
            case INDIVIDUAL -> RewardLimitsLang.EDITOR_UI_INVENTORY_LIMITS_BUTTON_INDIVIDUAL;
        };

        String skin = switch (type) {
            case GLOBAL -> "2287a796ddc859b48f6e3720604b9ec08562039265894ad24c407dafe79173af";
            case INDIVIDUAL -> "ad3c7cb7e8e908b75c5f35f397e0104bc78c43bfb53fea5def7b1c3c9dfe686";
        };

        int slot = switch (type) {
            case GLOBAL -> 11;
            case INDIVIDUAL -> 12;
        };

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.asCustomHead(skin)
                    .hideAllComponents()
                    .localized(locale)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> {
                            return CoreLang.STATE_ENABLED_DISALBED.get(snapshot.enabled());
                        })
                    )
                )
                .action(ctx -> this.handleLimitClick(ctx, type, snapshot))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void handleBack(ActionContext context) {
        RewardLimitsMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();

        RewardLimitsMainMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();

        if (this.controller.onLimitsMenuStateClick(player, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleLimitClick(ActionContext context, LimitType type, LimitSnapshot snapshot) {
        Player player = context.getPlayer();

        RewardLimitsMainMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();

        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onLimitsMenuLimitClick(player, hook, type, snapshot, refreshUI);
    }

    private void handleAlternativeState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();

        RewardLimitsMainMenuContext menuContext = this.getObject(context);
        RewardEditorHook hook = menuContext.hook();

        if (this.controller.onLimitsMenuAlternativeStateClick(player, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleAlternativeRewardId(ActionContext context) {
        Player player = context.getPlayer();

        RewardLimitsMainMenuContext menuContext = this.getObject(context);

        this.controller.onLimitsMenuAlternativeRewardIdClick(player, menuContext);
    }
}