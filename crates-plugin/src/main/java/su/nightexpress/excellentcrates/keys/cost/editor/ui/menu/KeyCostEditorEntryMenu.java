package su.nightexpress.excellentcrates.keys.cost.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementEntry;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.menu.context.KeyCostEntryMenuContext;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
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
public class KeyCostEditorEntryMenu extends AbstractObjectMenu<KeyCostEntryMenuContext> {

    private final KeyCostEditorUIController controller;

    public KeyCostEditorEntryMenu(CratesPlugin plugin,
                                  KeyCostEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, KeyCostLang.EDITOR_UI_INVENTORY_ENTRY_TITLE
            .text(), KeyCostEntryMenuContext.class);
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
        KeyCostEntryMenuContext menuContext = this.getObject(context);
        Identifier keyId = menuContext.keyId();
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        KeyRequirementComponent costComponent = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        if (costComponent == null) return;

        KeyRequirementEntry entry = costComponent.getKeyEntry(keyId);
        if (entry == null) return;

        int currentAmount = entry.getAmount();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.COMPARATOR)
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRY_BUTTON_AMOUNT)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> NumberUtil.format(currentAmount))
                    )
                )
                .action(actionContext -> this.handleAmountClick(actionContext, currentAmount))
                .build()
            )
            .slots(13)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .localized(KeyCostLang.EDITOR_UI_INVENTORY_ENTRY_BUTTON_REMOVE)
                    .hideAllComponents()
                )
                .action(actionContext -> this.handleRemoveClick(actionContext))
                .build()
            )
            .slots(35)
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
        KeyCostEntryMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleAmountClick(ActionContext context, int currentAmount) {
        Player player = context.getPlayer();
        KeyCostEntryMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        Identifier keyId = menuContext.keyId();
        CrateEditorHook hook = menuContext.hook();

        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onEntryMenuAmountClick(player, crate, hook, keyId, currentAmount, refreshUI);
    }

    private void handleRemoveClick(ActionContext context) {
        Player player = context.getPlayer();
        KeyCostEntryMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        this.controller.onEntryMenuRemoveClick(player, crate, menuContext);
    }
}
