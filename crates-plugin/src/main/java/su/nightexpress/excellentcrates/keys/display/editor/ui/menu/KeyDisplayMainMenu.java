package su.nightexpress.excellentcrates.keys.display.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.key.data.model.KeyDisplay;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.editor.ui.menu.context.KeyDisplayMainMenuContext;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyDisplayMainMenu extends AbstractObjectMenu<KeyDisplayMainMenuContext> {

    private final KeyResolver                  keyResolver;
    private final KeyDisplayEditorUIController controller;

    public KeyDisplayMainMenu(CratesPlugin plugin,
                              KeyResolver keyResolver,
                              KeyDisplayEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, KeyDisplayLang.EDITOR_UI_INVENTORY_MAIN_TITLE
            .text(), KeyDisplayMainMenuContext.class);
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
        KeyDisplayMainMenuContext menuContext = this.getObject(context);
        CrateKey key = this.keyResolver.resolveKey(menuContext.keyId());
        if (key == null) return;

        KeyDisplay display = key.getDisplay();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.NAME_TAG)
                    .localized(KeyDisplayLang.EDITOR_UI_INVENTORY_MAIN_BUTTON_NAME)
                    .replace(ctx -> ctx.with(CommonPlaceholders.GENERIC_VALUE, () -> display.getName()))
                    .hideAllComponents()
                )
                .action(actionContext -> this.handleNameClick(context, display.getName()))
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.WRITABLE_BOOK)
                    .localized(KeyDisplayLang.EDITOR_UI_INVENTORY_MAIN_BUTTON_LORE)
                    .replace(ctx -> ctx.with(CommonPlaceholders.GENERIC_VALUE, () -> {
                        return String.join("\n", display.getLore());
                    }))
                    .hideAllComponents()
                )
                .action(actionContext -> this.handleLoreClick(context, display.getLore()))
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

    private void handleBack(ViewerContext context) {
        KeyDisplayMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleNameClick(ViewerContext context, String currentName) {
        Player player = context.getPlayer();
        KeyDisplayMainMenuContext menuContext = this.getObject(context);
        KeyEditorHook hook = menuContext.hook();
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onDisplayMenuNameClick(player, hook, currentName, refreshUI);
    }

    private void handleLoreClick(ViewerContext context, List<String> currentLore) {
        Player player = context.getPlayer();
        KeyDisplayMainMenuContext menuContext = this.getObject(context);
        KeyEditorHook hook = menuContext.hook();
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onDisplayMenuLoreClick(player, hook, currentLore, refreshUI);
    }
}
