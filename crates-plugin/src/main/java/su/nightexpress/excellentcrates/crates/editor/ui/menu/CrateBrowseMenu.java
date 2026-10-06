package su.nightexpress.excellentcrates.crates.editor.ui.menu;

import java.util.Comparator;
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
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.editor.ui.CrateEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.ui.menu.context.CrateBrowseMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemPopulator;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class CrateBrowseMenu extends AbstractObjectMenu<CrateBrowseMenuContext> {

    private final CrateEditorUIController controller;
    private final CrateRegistry           registry;
    private final ICrateItemFactory       itemFactory;
    private final CratePlaceholders       cratePlaceholders;

    private final ItemPopulator<Crate> crateItemPopulator;

    public CrateBrowseMenu(CratesPlugin plugin,
                           CrateEditorUIController controller,
                           CrateRegistry registry,
                           ICrateItemFactory renderer,
                           CratePlaceholders cratePlaceholders) {
        super(plugin, MenuType.GENERIC_9X5, CrateEditorLang.UI_INVENTORY_CRATES_TITLE
            .text(), CrateBrowseMenuContext.class);
        this.controller = controller;
        this.registry = registry;
        this.itemFactory = renderer;
        this.cratePlaceholders = cratePlaceholders;

        this.crateItemPopulator = ItemPopulator.builder(Crate.class)
            .slots(IntStream.range(0, 36).toArray())
            .itemProvider((context, crate) -> {
                return this.itemFactory.renderCrateIcon(crate)
                    .localized(CrateEditorLang.UI_INVENTORY_CRATES_BUTTON_CRATE)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .apply(this.cratePlaceholders.basePlaceholders(crate))
                    );
            })
            .actionProvider(crate -> ctx -> this.handleCrateClick(ctx, crate))
            .build();
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 36).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(36, 45).toArray());

        this.addNextPageButton(41);
        this.addPreviousPageButton(39);

        this.addBackButton(this::handleBack, 36);

        this.addDefaultButton("creation", MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ANVIL)
                    .localized(CrateEditorLang.UI_INVENTORY_CRATES_BUTTON_CREATE)
                    .hideAllComponents()
                )
                .action(this::handleCreationClick)
                .build()
            )
            .slots(40)
            .build()
        );
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
        List<Crate> crates = this.registry.values()
            .stream()
            .sorted(Comparator.comparing(Crate::idString))
            .toList();

        this.crateItemPopulator.populateTo(context, crates, items);
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        CrateBrowseMenuContext menuContext = this.getObject(context);
        menuContext.moveBackward(context.getPlayer());
    }

    private void handleCrateClick(ActionContext context, Crate crate) {
        Player player = context.getPlayer();
        CrateBrowseMenuContext menuContext = this.getObject(context);

        this.controller.onBrowseMenuCrateClick(player, crate, menuContext);

    }

    private void handleCreationClick(ActionContext context) {
        Player player = context.getPlayer();
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onBrowseMenuCreateClick(player, refreshUI);
    }
}
