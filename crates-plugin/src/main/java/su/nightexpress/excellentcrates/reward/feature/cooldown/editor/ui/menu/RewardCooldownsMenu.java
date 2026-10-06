package su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.RewardCooldownsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.cooldown.editor.ui.menu.context.RewardCooldownsMenuContext;
import su.nightexpress.excellentcrates.reward.feature.cooldown.lang.RewardCooldownsLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.locale.entry.IconLocale;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardCooldownsMenu extends AbstractObjectMenu<RewardCooldownsMenuContext> {

    private final RewardCooldownsEditorUIController controller;

    public RewardCooldownsMenu(CratesPlugin plugin,
                               RewardCooldownsEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardCooldownsLang.EDITOR_UI_INVENTORY_COOLDOWNS_TITLE
            .text(), RewardCooldownsMenuContext.class);
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
        RewardCooldownsMenuContext menuContext = this.getObject(context);

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardCooldownComponent cooldowns = reward.getComponentOrNull(RewardComponentKeys.COOLDOWN);
        if (cooldowns == null) return;

        for (CooldownType type : CooldownType.values()) {
            CooldownSnapshot snapshot = switch (type) {
                case GLOBAL -> CooldownSnapshot.of(cooldowns.getGlobalCooldown());
                case INDIVIDUAL -> CooldownSnapshot.of(cooldowns.getIndividualCooldown());
            };

            items.add(this.crateCooldownButton(type, snapshot));
        }
    }

    private MenuItem crateCooldownButton(CooldownType type, CooldownSnapshot snapshot) {
        IconLocale locale = switch (type) {
            case GLOBAL -> RewardCooldownsLang.EDITOR_UI_INVENTORY_COOLDOWNS_GLOBAL_BUTTON;
            case INDIVIDUAL -> RewardCooldownsLang.EDITOR_UI_INVENTORY_COOLDOWNS_PLAYER_BUTTON;
        };

        String skin = switch (type) {
            case GLOBAL -> "2287a796ddc859b48f6e3720604b9ec08562039265894ad24c407dafe79173af";
            case INDIVIDUAL -> "ad3c7cb7e8e908b75c5f35f397e0104bc78c43bfb53fea5def7b1c3c9dfe686";
        };

        int slot = switch (type) {
            case GLOBAL -> 12;
            case INDIVIDUAL -> 14;
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
                .action(ctx -> this.handleCooldownClick(ctx, type, snapshot))
                .build()
            )
            .slots(slot)
            .build();
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardCooldownsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleCooldownClick(ActionContext context, CooldownType type, CooldownSnapshot snapshot) {
        Player player = context.getPlayer();
        RewardCooldownsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onCooldownsMenuCooldownClick(player, menuContext, type, snapshot, refreshUI);
    }
}
