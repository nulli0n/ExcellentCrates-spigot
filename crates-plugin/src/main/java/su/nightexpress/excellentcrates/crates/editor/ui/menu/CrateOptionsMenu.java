package su.nightexpress.excellentcrates.crates.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateOptionsMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class CrateOptionsMenu extends AbstractObjectMenu<CrateOptionsMenuContext> {

    private static final int[] EXTENSION_SLOTS = {
        10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    private final CrateRegistry                      registry;
    private final TinyRegistry<CrateEditorExtension> extensions;
    private final CrateEditorUIController            controller;

    public CrateOptionsMenu(CratesPlugin plugin,
                            CrateRegistry registry,
                            TinyRegistry<CrateEditorExtension> extensions,
                            CrateEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X6, CrateEditorLang.UI_INVENTORY_CRATE_OPTIONS_TITLE
            .text(), CrateOptionsMenuContext.class);
        this.registry = registry;
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
        CrateOptionsMenuContext menuContext = this.getObject(context);
        Identifier crateId = menuContext.crateId();
        Crate crate = this.registry.get(crateId);
        if (crate == null) return;

        this.renderButtons(items);
        this.renderExtensions(player, crate, inventory, menuContext, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void renderButtons(List<MenuItem> items) {
        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .hideAllComponents()
                    .localized(CrateEditorLang.UI_INVENTORY_CRATE_OPTIONS_BUTTON_DELETE)
                )
                .action(this::handleDelete)
                .build()
            )
            .slots(53)
            .build());
    }

    private void renderExtensions(Player player, Crate crate, Inventory inventory, CrateOptionsMenuContext menuContext,
                                  List<MenuItem> items) {
        Identifier crateId = crate.id();
        CrateEditorHook hook = modifier -> controller.onExtensionModify(player, crateId, modifier);

        BackwardNavigator backToCrateSettings = user -> {
            this.controller.backToCrateSettings(user, crateId, menuContext.backwardNavigator());
        };

        List<CrateEditorExtension> unpositionedExtensions = new ArrayList<>();
        Set<Integer> occupiedSlots = new HashSet<>();

        this.extensions.forEach(extension -> {
            if (extension instanceof PositionedExtension positioned) {
                int slot = positioned.getFixedSlot();
                if (slot < 0 || slot >= inventory.getSize() || occupiedSlots.contains(slot)) {
                    unpositionedExtensions.add(extension);
                    return;
                }

                items.add(extension.createButton(crate, hook, backToCrateSettings, slot));
                occupiedSlots.add(slot);
                return;
            }

            unpositionedExtensions.add(extension);
        });

        unpositionedExtensions.sort(Comparator.comparing(CrateEditorExtension::idString));

        for (int index = 0; index < unpositionedExtensions.size(); index++) {
            if (index >= EXTENSION_SLOTS.length) break;

            CrateEditorExtension extension = unpositionedExtensions.get(index);
            int slot = EXTENSION_SLOTS[index];

            items.add(extension.createButton(crate, hook, backToCrateSettings, slot));
        }
    }

    private void handleBack(ActionContext context) {
        CrateOptionsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleDelete(ActionContext context) {
        Player player = context.getPlayer();
        CrateOptionsMenuContext menuContext = this.getObject(context);
        Identifier crateId = menuContext.crateId();

        this.controller.onOptionsMenuDeleteClick(player, crateId, menuContext);
    }
}
