package su.nightexpress.excellentcrates.keys.editor.ui.menu;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.editor.ui.KeyEditorUIController;
import su.nightexpress.excellentcrates.keys.editor.ui.menu.context.KeyBrowseMenuContext;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class KeyBrowseMenu extends AbstractObjectMenu<KeyBrowseMenuContext> {

    private final KeyRegistry           keyRegistry;
    private final KeyItemFactory        itemFactory;
    private final KeyEditorUIController controller;

    private final ItemPopulator<Identifier> keyItemPopulator;

    public KeyBrowseMenu(CratesPlugin plugin,
                         KeyRegistry keyRegistry,
                         KeyItemFactory itemFactory,
                         KeyEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, KeyEditorLang.UI_INVENTORY_BROWSE_TITLE.text(), KeyBrowseMenuContext.class);
        this.keyRegistry = keyRegistry;
        this.itemFactory = itemFactory;
        this.controller = controller;

        this.keyItemPopulator = ItemPopulator.builder(Identifier.class)
            .itemProvider((context, id) -> this.createKeyItem(id))
            .actionProvider(keyId -> actionContext -> this.handleKeyClick(actionContext, keyId))
            .slots(IntStream.range(0, 36).toArray())
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addBackButton(this::handleBack, 36);

        this.addDefaultButton("manual_creation", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .localized(KeyEditorLang.UI_INVENTORY_BROWSE_BUTTON_MANUAL_CREATION)
                )
                .action(this::handleManualCreation)
                .build()
            )
            .slots(41)
            .build()
        );

        this.addDefaultButton("automatic_creation", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENCHANTING_TABLE)
                    .localized(KeyEditorLang.UI_INVENTORY_BROWSE_BUTTON_AUTOMATIC_CREATION)
                )
                .build()
            )
            .slots(39)
            .build()
        );
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if (slot < inventory.getSize()) return;

        // Reset click cooldown to allow fast double clicks.
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            this.handleAutomaticCreation(context, new ItemStack(itemStack));
        }
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
        List<Identifier> keyIds = this.keyRegistry.keys()
            .stream()
            .sorted(Comparator.comparing(Identifier::value))
            .toList();

        this.keyItemPopulator.populateTo(context, keyIds, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private @Nullable NightItem createKeyItem(Identifier keyId) {
        CrateKey key = this.keyRegistry.get(keyId);
        if (key == null) return null;

        return this.itemFactory.createDisplayIcon(key)
            .localized(KeyEditorLang.UI_INVENTORY_BROWSE_KEY)
            .hideAllComponents();
    }

    private void handleBack(ViewerContext context) {
        KeyBrowseMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleKeyClick(ActionContext context, Identifier keyId) {
        KeyBrowseMenuContext menuContext = this.getObject(context);

        this.controller.onListMenuKeyClick(context.getPlayer(), keyId, menuContext);
    }

    private void handleManualCreation(ActionContext context) {
        Player player = context.getPlayer();
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onManualCreationClick(player, refreshUI);
    }

    private void handleAutomaticCreation(ViewerContext context, ItemStack itemStack) {
        Player player = context.getPlayer();
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onAutomaticCreationClick(player, itemStack, refreshUI);
    }
}
