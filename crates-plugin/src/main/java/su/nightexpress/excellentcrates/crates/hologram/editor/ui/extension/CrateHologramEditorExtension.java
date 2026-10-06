package su.nightexpress.excellentcrates.crates.hologram.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.CrateHologramEditorUIController;
import su.nightexpress.excellentcrates.crates.hologram.lang.HologramsLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class CrateHologramEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("hologram");

    private final CrateHologramEditorUIController controller;

    public CrateHologramEditorExtension(CrateHologramEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator backToCrateSettings,
                                 int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ARMOR_STAND)
                    .hideAllComponents()
                    .localized(HologramsLang.UI_EXTENSION_BUTTON)
                )
                .action(context -> this.click(context, crate.id(), hook, backToCrateSettings))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Identifier crateId, CrateEditorHook hook,
                       BackwardNavigator backwardNavigator) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, crateId, hook, backwardNavigator);
    }
}
