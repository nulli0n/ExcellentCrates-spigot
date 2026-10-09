package su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.RewardComponentEditorUIController;
import su.nightexpress.excellentcrates.reward.crate.component.editor.ui.menu.context.RewardComponentSettingsMenuContext;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardComponentSettingsMenu extends AbstractObjectMenu<RewardComponentSettingsMenuContext> {

    private final RewardComponentEditorUIController controller;

    public RewardComponentSettingsMenu(CratesPlugin plugin,
                                       RewardComponentEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardComponentLang.EDITOR_UI_INVENTORY_SETTINGS_TITLE
            .text(), RewardComponentSettingsMenuContext.class);
        this.controller = controller;
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 27);

        this.addDefaultButton("rewards", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.VAULT)
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_REWARDS)
                )
                .action(this::handleRewards)
                .build()
            )
            .slots(14)
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
        RewardComponentSettingsMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateRewardsComponent crateRewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
        if (crateRewards == null) return;

        int rollCount = crateRewards.getRollCount();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.COMPARATOR)
                    .hideAllComponents()
                    .localized(RewardComponentLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_ROLL_COUNT)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> NumberUtil.format(rollCount))
                    )
                )
                .action(ctx -> this.handleRollCount(ctx, rollCount))
                .build()
            )
            .slots(12)
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
        RewardComponentSettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleRewards(ActionContext context) {
        Player player = context.getPlayer();
        RewardComponentSettingsMenuContext menuContext = this.getObject(context);

        this.controller.onSettingsRewardsClick(player, menuContext);
    }

    private void handleRollCount(ActionContext context, int current) {
        Player player = context.getPlayer();
        RewardComponentSettingsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onSettingsRollCountClick(player, menuContext, current, refreshUI);
    }
}
