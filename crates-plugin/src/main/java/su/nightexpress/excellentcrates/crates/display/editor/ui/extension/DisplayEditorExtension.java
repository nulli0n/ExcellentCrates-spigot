package su.nightexpress.excellentcrates.crates.display.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.display.editor.ui.DisplayEditorUIController;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class DisplayEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("display");

    private final DisplayEditorUIController controller;

    public DisplayEditorExtension(DisplayEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot) {
        return MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.NAME_TAG)
                    .hideAllComponents()
                    .localized(CrateEditorLang.UI_INVENTORY_CRATE_OPTIONS_BUTTON_DISPLAY)
                )
                .action(context -> this.click(context, crate.id(), hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Identifier crateId, CrateEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, crateId, hook, navigator);
    }
}
