package su.nightexpress.excellentcrates.crates.display.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.data.model.ICrateDisplay;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.display.editor.ui.menu.context.CrateDisplaySettingsMenuContext;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class CrateDisplaySettingsMenu extends AbstractObjectMenu<CrateDisplaySettingsMenuContext> {

    private final CrateResolver             crateResolver;
    private final DisplayEditorUIController controller;

    public CrateDisplaySettingsMenu(CratesPlugin plugin,
                                    CrateResolver crateResolver,
                                    DisplayEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, CrateEditorLang.UI_INVENTORY_CRATE_DISPLAY_TITLE
            .text(), CrateDisplaySettingsMenuContext.class);
        this.crateResolver = crateResolver;
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
        CrateDisplaySettingsMenuContext menuContext = this.getObject(context);
        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) return;

        ICrateDisplay display = crate.getDisplay();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.NAME_TAG)
                    .hideAllComponents()
                    .localized(CrateEditorLang.UI_INVENTORY_CRATE_DISPLAY_BUTTON_NAME)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, display::getName)
                    )
                )
                .action(actionContext -> this.handleName(actionContext, display))
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.WRITABLE_BOOK)
                    .hideAllComponents()
                    .localized(CrateEditorLang.UI_INVENTORY_CRATE_DISPLAY_BUTTON_LORE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> String.join(TagWrappers.BR, display.getLore()))
                    )
                )
                .action(actionContext -> this.handleLore(actionContext, display))
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

    private void handleBack(ActionContext context) {
        CrateDisplaySettingsMenuContext menuContext = this.getObject(context);

        menuContext.backwardNavigator().moveBack(context.getPlayer());
    }

    private void handleName(ActionContext context, ICrateDisplay display) {
        Player player = context.getPlayer();
        CrateDisplaySettingsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onSettingsMenuNameClick(player, menuContext, display, refreshUI);
    }

    private void handleLore(ActionContext context, ICrateDisplay display) {
        Player player = context.getPlayer();
        CrateDisplaySettingsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onSettingsMenuLoreClick(player, menuContext, display, refreshUI);
    }
}
