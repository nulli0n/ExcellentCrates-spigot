package su.nightexpress.excellentcrates.crates.cooldown.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIController;
import su.nightexpress.excellentcrates.crates.cooldown.lang.CrateCooldownsLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class CrateCooldownsEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("cooldown");

    private final CrateCooldownsEditorUIController controller;

    public CrateCooldownsEditorExtension(CrateCooldownsEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator backwardNavigator, int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.CLOCK)
                    .hideAllComponents()
                    .localized(CrateCooldownsLang.UI_EXTENSION_BUTTON)
                )
                .action(ctx -> this.click(ctx, crate.id(), hook, backwardNavigator))
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
