package su.nightexpress.excellentcrates.effect.crate.editor.ui.menu;

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
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.excellentcrates.api.effect.EffectRegistry;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.menu.context.EffectComponentEditorMainMenuContext;
import su.nightexpress.excellentcrates.effect.lang.EffectsLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
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
public class EffectComponentEditorMainMenu extends AbstractObjectMenu<EffectComponentEditorMainMenuContext> {

    private final EffectRegistry                    effectRegistry;
    private final EffectComponentEditorUIController uiController;

    public EffectComponentEditorMainMenu(CratesPlugin plugin,
                                         EffectRegistry effectRegistry,
                                         EffectComponentEditorUIController uiController) {
        super(plugin, MenuType.GENERIC_9X4, EffectsLang.EDITOR_UI_INVENTORY_COMPONENT_TITLE
            .text(), EffectComponentEditorMainMenuContext.class);
        this.effectRegistry = effectRegistry;
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
        EffectComponentEditorMainMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        EffectComponent component = crate.getComponentOrNull(CrateComponentKeys.EFFECT);
        if (component == null) return;

        boolean currentState = component.isEnabled();
        AdaptedKey currentKey = component.getProfileKey();
        EffectProfile<?> profile = this.effectRegistry.getProfile(currentKey);

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.LIME_DYE)
                    .hideAllComponents()
                    .localized(EffectsLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, false))
                .condition(ctx -> currentState)
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.GRAY_DYE)
                    .hideAllComponents()
                    .localized(EffectsLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_STATE)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .action(ctx -> this.handleComponentState(ctx, true))
                .condition(ctx -> !currentState)
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BLAZE_POWDER)
                    .hideAllComponents()
                    .localized(EffectsLang.EDITOR_UI_INVENTORY_COMPONENT_BUTTON_PROFILE)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            if (profile == null) {
                                return CoreLang.badEntry(currentKey.asString());
                            }
                            return CoreLang.goodEntry(profile.baseSettings().name());
                        })
                    )
                )
                .action(ctx -> this.handleComponentProfileKey(ctx, currentKey))
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
        EffectComponentEditorMainMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleComponentState(ActionContext context, boolean newState) {
        Player player = context.getPlayer();
        EffectComponentEditorMainMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateEditorHook hook = menuContext.hook();

        if (this.uiController.onComponentMenuStateClick(player, crate, hook, newState)) {
            this.refresh(player);
        }
    }

    private void handleComponentProfileKey(ActionContext context, AdaptedKey currentKey) {
        Player player = context.getPlayer();
        EffectComponentEditorMainMenuContext menuContext = this.getObject(context);
        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        CrateEditorHook hook = menuContext.hook();

        Runnable refreshUI = () -> this.refresh(player);

        this.uiController.onComponentMenuProfileClick(player, crate, hook, currentKey, refreshUI);
    }
}
