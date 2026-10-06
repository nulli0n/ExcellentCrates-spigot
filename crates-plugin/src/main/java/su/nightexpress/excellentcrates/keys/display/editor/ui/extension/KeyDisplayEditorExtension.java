package su.nightexpress.excellentcrates.keys.display.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.data.extension.PositionedExtension;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.display.editor.ui.KeyDisplayEditorUIController;
import su.nightexpress.excellentcrates.keys.display.lang.KeyDisplayLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class KeyDisplayEditorExtension implements KeyEditorExtension, PositionedExtension {

    private static final Identifier ID = new Identifier("display");

    private final KeyDisplayEditorUIController controller;

    public KeyDisplayEditorExtension(KeyDisplayEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public int getFixedSlot() {
        return 13;
    }

    @Override
    public MenuItem createButton(CrateKey key, KeyEditorHook hook, BackwardNavigator backNavigator, int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ITEM_FRAME)
                    .localized(KeyDisplayLang.EDITOR_UI_EXTENSION_BUTTON)
                    .hideAllComponents()
                )
                .action(actionContext -> this.click(actionContext, key, hook, backNavigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    private void click(ActionContext context, CrateKey key, KeyEditorHook hook, BackwardNavigator backNavigator) {
        Player player = context.getPlayer();
        this.controller.onExtensionClick(player, key.id(), hook, backNavigator);
    }
}
