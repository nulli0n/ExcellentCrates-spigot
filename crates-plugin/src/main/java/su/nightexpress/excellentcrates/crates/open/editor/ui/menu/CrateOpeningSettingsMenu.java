package su.nightexpress.excellentcrates.crates.open.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.crates.open.editor.ui.CrateOpeningEditorUIController;
import su.nightexpress.excellentcrates.crates.open.editor.ui.menu.context.CrateOpeningSettingsMenuContext;
import su.nightexpress.excellentcrates.crates.open.lang.CrateOpeningLang;
import su.nightexpress.excellentcrates.util.UIUtils;
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
public class CrateOpeningSettingsMenu extends AbstractObjectMenu<CrateOpeningSettingsMenuContext> {

    private final CrateResolver                  crateResolver;
    private final CrateOpeningEditorUIController controller;

    public CrateOpeningSettingsMenu(CratesPlugin plugin,
                                    CrateResolver crateResolver,
                                    CrateOpeningEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, CrateOpeningLang.EDITOR_UI_INVENTORY_SETTINGS_TITLE
            .text(), CrateOpeningSettingsMenuContext.class);
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
        CrateOpeningSettingsMenuContext menuContext = this.getObject(context);
        Identifier crateId = menuContext.crateId();

        Crate crate = this.crateResolver.resolveCrate(crateId);
        if (crate == null) return;

        OpenActionsComponent component = crate.getComponentOrNull(CrateComponentKeys.OPEN_ACTIONS);
        if (component == null) return;

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(CrateOpeningLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleState(ctx, false))
                .condition(ctx -> component.isEnabled())
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(CrateOpeningLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleState(ctx, true))
                .condition(ctx -> !component.isEnabled())
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.COMMAND_BLOCK_MINECART)
                    .hideAllComponents()
                    .localized(CrateOpeningLang.EDITOR_UI_INVENTORY_SETTINGS_BUTTON_COMMANDS)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return UIUtils.formatCommandList(component.getCommands());
                        })
                    )
                )
                .action(ctx -> this.handleCommands(ctx, component.getCommands()))
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
        CrateOpeningSettingsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        CrateOpeningSettingsMenuContext menuContext = this.getObject(context);
        CrateEditorHook hook = menuContext.hook();

        if (this.controller.onSettingsMenuStateClick(player, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleCommands(ActionContext context, List<String> currentCommands) {
        Player player = context.getPlayer();
        CrateOpeningSettingsMenuContext menuContext = this.getObject(context);
        CrateEditorHook hook = menuContext.hook();
        Runnable refresh = () -> this.refresh(player);

        this.controller.onSettingsMenuCommandsClick(player, hook, currentCommands, refresh);
    }
}
