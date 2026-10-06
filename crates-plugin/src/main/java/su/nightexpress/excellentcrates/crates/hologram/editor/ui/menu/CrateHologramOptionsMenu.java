package su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramComponent;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.context.CrateHologramOptionsMenuContext;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CrateHologramOptionsMenu extends AbstractObjectMenu<CrateHologramOptionsMenuContext> {

    private final CrateResolver                   crateResolver;
    private final CrateHologramEditorUIController uiController;

    public CrateHologramOptionsMenu(CratesPlugin plugin,
                                    CrateResolver crateResolver,
                                    CrateHologramEditorUIController uiController) {
        super(plugin, MenuType.GENERIC_9X4, HologramsLang.UI_INVENTORY_OPTIONS_TITLE
            .text(), CrateHologramOptionsMenuContext.class);
        this.crateResolver = crateResolver;
        this.uiController = uiController;
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
        CrateHologramOptionsMenuContext menuContext = this.getObject(context);
        Identifier crateId = menuContext.crateId();

        Crate crate = this.crateResolver.resolveCrate(crateId);
        if (crate == null) return;

        HologramComponent component = crate.getComponentOrNull(CrateComponentKeys.HOLOGRAM);
        if (component == null) return;

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(HologramsLang.UI_INVENTORY_OPTIONS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleState(ctx, true))
                .condition(ctx -> component.isEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(HologramsLang.UI_INVENTORY_OPTIONS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleState(ctx, false))
                .condition(ctx -> !component.isEnabled())
                .build()
            )
            .slots(11)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.PAPER)
                    .hideAllComponents()
                    .localized(HologramsLang.UI_INVENTORY_OPTIONS_BUTTON_TEXT)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> String.join("\n", component.getText()))
                    )
                )
                .action(ctx -> this.handleText(ctx, component.getText()))
                .build()
            )
            .slots(13)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ARMOR_STAND)
                    .hideAllComponents()
                    .localized(HologramsLang.UI_INVENTORY_OPTIONS_BUTTON_OFFSET)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.X, () -> String.valueOf(component.getOffset().getX()))
                        .with(SharedPlaceholders.Y, () -> String.valueOf(component.getOffset().getY()))
                        .with(SharedPlaceholders.Z, () -> String.valueOf(component.getOffset().getZ()))
                    )
                )
                .action(ctx -> this.handleOffset(ctx, component.getOffset()))
                .build()
            )
            .slots(15)
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
        Player player = context.getPlayer();
        CrateHologramOptionsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(player);
    }

    private void handleState(ActionContext context, boolean currentState) {
        Player player = context.getPlayer();
        CrateHologramOptionsMenuContext menuContext = this.getObject(context);

        boolean newState = !currentState;

        this.uiController.onHologramOptionsStateClick(player, menuContext, newState);
        this.refresh(player);
    }

    private void handleText(ActionContext context, List<String> currentText) {
        Player player = context.getPlayer();
        CrateHologramOptionsMenuContext menuContext = this.getObject(context);

        this.uiController.onHologramOptionsTextClick(player, menuContext, currentText, () -> {
            this.refresh(player);
        });
    }

    private void handleOffset(ActionContext context, HologramOffset currentOffset) {
        Player player = context.getPlayer();
        CrateHologramOptionsMenuContext menuContext = this.getObject(context);

        this.uiController.onHologramOptionsOffsetClick(player, menuContext, currentOffset, () -> {
            this.refresh(player);
        });
    }
}
