package su.nightexpress.excellentcrates.keys.item.editor.ui.extension;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.data.extension.PositionedExtension;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.editor.lang.KeyEditorLang;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;
import su.nightexpress.excellentcrates.keys.item.editor.ui.KeyItemEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;

@NullMarked
public class KeyItemEditorExtension implements KeyEditorExtension, PositionedExtension {

    private static final Identifier ID = new Identifier("item");

    private final KeyItemFactory            itemFactory;
    private final KeyItemEditorUIController controller;

    public KeyItemEditorExtension(KeyItemFactory itemFactory, KeyItemEditorUIController controller) {
        this.itemFactory = itemFactory;
        this.controller = controller;
    }


    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public int getFixedSlot() {
        return 15;
    }

    @Override
    public MenuItem createButton(CrateKey key, KeyEditorHook hook, BackwardNavigator navigator, int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(this.itemFactory.createDisplayIcon(key)
                    .hideAllComponents()
                    .localized(KeyEditorLang.UI_INVENTORY_SETTINGS_BUTTON_ITEM)
                )
                .action(actionContext -> this.click(actionContext, key, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, CrateKey key, KeyEditorHook hook, BackwardNavigator navigator) {
        this.controller.onExtensionClick(context.getPlayer(), key.getId(), hook, navigator);
    }
}
