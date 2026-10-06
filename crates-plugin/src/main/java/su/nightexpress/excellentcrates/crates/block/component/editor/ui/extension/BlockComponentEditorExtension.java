package su.nightexpress.excellentcrates.crates.block.component.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.crates.block.component.editor.ui.BlockEditorUIController;
import su.nightexpress.excellentcrates.crates.block.lang.BlocksLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class BlockComponentEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("block");

    private final BlockEditorUIController controller;

    public BlockComponentEditorExtension(BlockEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot) {
        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_CHEST)
                    .localized(BlocksLang.EDITOR_UI_EXTENSION_BUTTON)
                    .hideAllComponents()
                )
                .action(actionContext -> this.click(actionContext, crate.id(), hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Identifier crateId, CrateEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.controller.onCrateEditorBlockComponentClick(player, crateId, hook, navigator);
    }
}
