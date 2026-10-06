package su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.RewardBroadcastEditorUIController;
import su.nightexpress.excellentcrates.reward.broadcast.editor.ui.menu.context.BroadcastSettingsMenuContext;
import su.nightexpress.excellentcrates.reward.broadcast.lang.RewardBroadcastLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class BroadcastSettingsMenu extends AbstractObjectMenu<BroadcastSettingsMenuContext> {

    private final RewardBroadcastEditorUIController controller;

    public BroadcastSettingsMenu(CratesPlugin plugin,
                                 RewardBroadcastEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardBroadcastLang.EDITOR_UI_INVENTORY_SETTINGS_TITLE
            .text(), BroadcastSettingsMenuContext.class);
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
        BroadcastSettingsMenuContext menuContext = this.getObject(context);

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardBroadcastComponent component = reward.getComponentOrNull(RewardComponentKeys.BROADCAST);
        if (component == null) return;

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(RewardBroadcastLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleWinBroadcastClick(ctx, false))
                .condition(ctx -> component.isEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(RewardBroadcastLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleWinBroadcastClick(ctx, true))
                .condition(ctx -> !component.isEnabled())
                .build()
            )
            .slots(13)
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
        BroadcastSettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleWinBroadcastClick(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        BroadcastSettingsMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardEditorHook hook = menuContext.hook();

        if (this.controller.onBaseMenuWinBroadcastClick(player, reward, hook, newState)) {
            this.refresh(player);
        }
    }
}
