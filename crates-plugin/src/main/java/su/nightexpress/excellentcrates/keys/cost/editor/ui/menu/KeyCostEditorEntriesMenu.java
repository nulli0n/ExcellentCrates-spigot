package su.nightexpress.excellentcrates.keys.cost.editor.ui.menu;

import java.util.ArrayList;
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
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntriesMenuContext;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyCostEditorEntriesMenu extends AbstractObjectMenu<KeyCostEntriesMenuContext> {

    private final KeyRegistry               keyRegistry;
    private final KeyItemFactory            keyItemFactory;
    private final KeyCostEditorUIController controller;

    private final ItemPopulator<EntryWrapper> entryPopulator;

    private record EntryWrapper(Identifier keyId, KeyRequirementEntry entry) {
    }

    public KeyCostEditorEntriesMenu(CratesPlugin plugin,
                                    KeyRegistry keyRegistry,
                                    KeyItemFactory keyItemFactory,
                                    KeyCostEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X5, KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_TITLE
            .text(), KeyCostEntriesMenuContext.class);
        this.keyRegistry = keyRegistry;
        this.keyItemFactory = keyItemFactory;
        this.controller = controller;

        this.entryPopulator = ItemPopulator.builder(EntryWrapper.class)
            .itemProvider((context, wrapper) -> this.createEntryItem(wrapper))
            .actionProvider(wrapper -> context -> this.handleEntryClick(context, wrapper))
            .slots(IntStream.range(9, 36).toArray())
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(9, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 9).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addBackButton(this::handleBack, 36);
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
        KeyCostEntriesMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        KeyRequirementComponent crateKeys = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        if (crateKeys == null) return;

        List<EntryWrapper> wrappers = new ArrayList<>();
        Set<Identifier> existingKeys = new HashSet<>();

        crateKeys.getKeyEntryMap().forEach((keyId, entry) -> {
            existingKeys.add(keyId);
            wrappers.add(new EntryWrapper(keyId, entry));
        });

        this.entryPopulator.populateTo(context, wrappers, items);

        boolean currentState = crateKeys.isEnabled();
        boolean canAdd = !existingKeys.containsAll(this.keyRegistry.keys()) && !this.keyRegistry.values().isEmpty();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .condition(ctx -> currentState)
                .action(ctx -> this.handleStateClick(ctx, false))
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .condition(ctx -> !currentState)
                .action(ctx -> this.handleStateClick(ctx, true))
                .build()
            )
            .slots(4)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .hideAllComponents()
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ADD_ALLOWED)
                )
                .condition(ctx -> canAdd)
                .action(ctx -> this.handleAddClick(ctx, existingKeys))
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.DAMAGED_ANVIL)
                    .hideAllComponents()
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ADD_DISALLOWED)
                )
                .condition(ctx -> !canAdd)
                .build()
            )
            .slots(40)
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
        KeyCostEntriesMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private NightItem createEntryItem(EntryWrapper wrapper) {
        CrateKey key = this.keyRegistry.resolveKey(wrapper.keyId());
        if (key == null) {
            return NightItem.fromType(Material.BARRIER)
                .hideAllComponents()
                .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ENTRY_INVALID)
                .replace(ctx -> ctx
                    .with(SharedPlaceholders.KEY_ID, () -> wrapper.keyId().value())
                    .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(wrapper.entry().getAmount()))
                );
        }

        return this.keyItemFactory.createDisplayIcon(key, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(wrapper.entry().getAmount()))
        )
            .hideAllComponents()
            .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRIES_BUTTON_ENTRY_VALID);
    }

    private void handleStateClick(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        KeyCostEntriesMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        if (this.controller.onEntriesMenuCostEnabledClick(player, crate, menuContext.hook(), newState)) {
            this.refresh(player);
        }
    }

    private void handleAddClick(ActionContext context, Set<Identifier> existingKeys) {
        Player player = context.getPlayer();
        KeyCostEntriesMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        if (existingKeys.containsAll(this.keyRegistry.keys()) || this.keyRegistry.values().isEmpty()) {
            this.refresh(player);
            return;
        }

        this.controller.onEntriesMenuAddClick(player, crate, menuContext.hook(), existingKeys, () -> {
            this.refresh(player);
        });
    }

    private void handleEntryClick(ViewerContext context, EntryWrapper wrapper) {
        Player player = context.getPlayer();
        KeyCostEntriesMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        this.controller.onEntriesMenuEntryClick(player, crate, menuContext, wrapper.keyId());
    }
}
