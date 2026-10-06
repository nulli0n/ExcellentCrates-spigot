package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu;

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

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityResolver;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.RarityComponentEditorUIController;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu.context.RarityComponentMainMenuContext;
import su.nightexpress.excellentcrates.rarity.reward.component.lang.RarityComponentLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RarityComponentEditorMainMenu extends AbstractObjectMenu<RarityComponentMainMenuContext> {

    private final RarityResolver                    rarityResolver;
    private final RarityComponentEditorUIController uiController;

    public RarityComponentEditorMainMenu(CratesPlugin plugin,
                                         RarityResolver rarityResolver,
                                         RarityComponentEditorUIController uiController) {
        super(plugin, MenuType.GENERIC_9X4, RarityComponentLang.EDITOR_UI_INVENTORY_COMPONENT_TITLE
            .text(), RarityComponentMainMenuContext.class);
        this.rarityResolver = rarityResolver;
        this.uiController = uiController;
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
        RarityComponentMainMenuContext menuContext = this.getObject(context);

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
        if (component == null) return;

        boolean currentState = component.isEnabled();
        Identifier currentId = component.getRarityId();
        Rarity rarity = this.rarityResolver.resolveRarity(currentId);

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(RarityComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, false))
                .condition(ctx -> currentState)
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(RarityComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, true))
                .condition(ctx -> !currentState)
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOW_ITEM_FRAME)
                    .hideAllComponents()
                    .localized(RarityComponentLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_RARITY)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            if (rarity == null) {
                                return CoreLang.badEntry(currentId.value());
                            }
                            return CoreLang.goodEntry(rarity.getName());
                        })
                    )
                )
                .action(ctx -> this.handleComponentAnimationKey(ctx, currentId))
                .build()
            )
            .slots(14)
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
        RarityComponentMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleComponentState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        RarityComponentMainMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardEditorHook hook = menuContext.hook();

        if (this.uiController.onMainMenuStateClick(player, reward, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleComponentAnimationKey(ActionContext context, Identifier currentKey) {
        Player player = context.getPlayer();
        RarityComponentMainMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardEditorHook hook = menuContext.hook();
        Runnable refreshUI = () -> this.refresh(player);

        this.uiController.onComponentMenuRarityClick(player, reward, hook, currentKey, refreshUI);
    }
}
