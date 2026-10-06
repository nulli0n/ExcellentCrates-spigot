package su.nightexpress.excellentcrates.keys.common.base.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.data.extension.PositionedExtension;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorExtension;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.KeyBaseEditorUIController;
import su.nightexpress.excellentcrates.keys.common.base.lang.KeyBaseLang;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class KeyBaseEditorExtension implements KeyEditorExtension, PositionedExtension {

    private static final Identifier ID = new Identifier("base");

    private final KeyBaseEditorUIController controller;

    public KeyBaseEditorExtension(KeyBaseEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public MenuItem createButton(CrateKey key, KeyEditorHook hook, BackwardNavigator navigator, int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.PAPER)
                    .localized(KeyBaseLang.EDITOR_UI_EXTENSION_BUTTON)
                    .hideAllComponents()
                )
                .action(actionContext -> this.click(actionContext.getPlayer(), key, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public int getFixedSlot() {
        return 11;
    }

    private void click(Player player, CrateKey key, KeyEditorHook hook, BackwardNavigator navigator) {
        this.controller.onExtensionClick(player, key.getId(), hook, navigator);
    }
}
