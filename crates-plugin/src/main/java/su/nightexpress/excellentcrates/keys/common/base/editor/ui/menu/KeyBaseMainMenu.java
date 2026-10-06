package su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBase;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIController;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu.context.KeyBaseMainMenuContext;
import su.nightexpress.excellentcrates.keys.common.base.lang.KeyBaseLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class KeyBaseMainMenu extends AbstractObjectMenu<KeyBaseMainMenuContext> {

    private final KeyResolver               keyResolver;
    private final KeyBaseEditorUIController controller;

    public KeyBaseMainMenu(CratesPlugin plugin, KeyResolver keyResolver, KeyBaseEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, KeyBaseLang.EDITOR_UI_INVENTORY_MAIN_TITLE
            .text(), KeyBaseMainMenuContext.class);
        this.keyResolver = keyResolver;
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
        KeyBaseMainMenuContext menuContext = this.getObject(context);
        CrateKey key = this.keyResolver.resolveKey(menuContext.keyId());
        if (key == null) return;

        KeyBase base = key.getBase();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.TRIAL_KEY)
                    .localized(KeyBaseLang.UI_INVENTORY_MAIN_BUTTON_VIRTUAL_OFF)
                    .hideAllComponents()
                )
                .condition(ctx -> !base.isVirtual())
                .action(actionContex -> this.handleVirtualToggle(actionContex, true))
                .build()
            )
            .state("virtual", ItemState.builder()
                .icon(NightItem.fromType(Material.OMINOUS_TRIAL_KEY)
                    .localized(KeyBaseLang.UI_INVENTORY_MAIN_BUTTON_VIRTUAL_ON)
                    .hideAllComponents()
                )
                .condition(ctx -> base.isVirtual())
                .action(actionContex -> this.handleVirtualToggle(actionContex, false))
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
        KeyBaseMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleVirtualToggle(ActionContext context, boolean state) {
        Player player = context.getPlayer();
        KeyBaseMainMenuContext menuContext = this.getObject(context);
        KeyEditorHook hook = menuContext.hook();

        if (this.controller.onBaseVirtualClick(player, hook, state)) {
            this.refresh(player);
        }
    }
}
