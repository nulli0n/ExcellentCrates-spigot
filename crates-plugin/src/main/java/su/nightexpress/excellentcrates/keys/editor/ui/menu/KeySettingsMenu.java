package su.nightexpress.excellentcrates.keys.editor.ui.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.data.extension.PositionedExtension;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeySettingsMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class KeySettingsMenu extends AbstractObjectMenu<KeySettingsMenuContext> {

    private static final int[] EXTENSION_SLOTS = new int[]{
        28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43
    };

    private final KeyResolver                      keyResolver;
    private final TinyRegistry<KeyEditorExtension> extensions;
    private final KeyEditorUIController            controller;

    public KeySettingsMenu(CratesPlugin plugin,
                           KeyResolver keyResolver,
                           TinyRegistry<KeyEditorExtension> extensions,
                           KeyEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X6, KeyEditorLang.UI_INVENTORY_SETTINGS_TITLE
            .text(), KeySettingsMenuContext.class);
        this.keyResolver = keyResolver;
        this.extensions = extensions;
        this.controller = controller;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addBackButton(this::handleBack, 45);

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
        Player player = context.getPlayer();

        KeySettingsMenuContext menuContext = this.getObject(context);
        CrateKey key = this.keyResolver.resolveKey(menuContext.keyId());
        if (key == null) return;

        items.addAll(this.renderExtensions(player, key, inventory, menuContext));

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .localized(KeyEditorLang.UI_INVENTORY_SETTINGS_BUTTON_DELETE)
                    .hideAllComponents()
                )
                .action(this::handleDelete)
                .build()
            )
            .slots(53)
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
        KeySettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private List<MenuItem> renderExtensions(Player player, CrateKey key, Inventory inventory,
                                            KeySettingsMenuContext menuContext) {
        List<MenuItem> items = new ArrayList<>();

        KeyEditorHook hook = action -> this.controller.onExtensionModify(player, key.id(), action);
        BackwardNavigator navigator = user -> this.controller.onExtensionMoveBackward(user, menuContext);

        List<KeyEditorExtension> unpositionedExtensions = new ArrayList<>();
        Set<Integer> occupiedSlots = new HashSet<>();

        this.extensions.forEach(extension -> {
            if (extension instanceof PositionedExtension positioned) {
                int slot = positioned.getFixedSlot();
                if (slot < 0 || slot >= inventory.getSize() || occupiedSlots.contains(slot)) {
                    unpositionedExtensions.add(extension);
                    return;
                }

                items.add(extension.createButton(key, hook, navigator, slot));
                occupiedSlots.add(slot);
                return;
            }

            unpositionedExtensions.add(extension);
        });

        unpositionedExtensions.sort(Comparator.comparing(KeyEditorExtension::idString));

        for (int index = 0; index < unpositionedExtensions.size(); index++) {
            if (index >= EXTENSION_SLOTS.length) break;

            KeyEditorExtension extension = unpositionedExtensions.get(index);
            int slot = EXTENSION_SLOTS[index];

            items.add(extension.createButton(key, hook, navigator, slot));
        }

        return items;
    }

    private void handleDelete(ActionContext context) {
        Player player = context.getPlayer();
        KeySettingsMenuContext menuContext = this.getObject(context);

        Identifier keyId = menuContext.keyId();
        CrateKey key = this.keyResolver.resolveKey(keyId);
        if (key == null) return;

        this.controller.onOptionsMenuDeleteClick(player, menuContext);
    }
}
