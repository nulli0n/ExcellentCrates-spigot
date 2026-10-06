package su.nightexpress.excellentcrates.crates.item.editor.ui.extension;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.item.ICrateItemFactory;
import su.nightexpress.excellentcrates.crates.editor.lang.CrateEditorLang;
import su.nightexpress.excellentcrates.crates.item.editor.ui.CrateItemEditorUIController;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;

@NullMarked
public class CrateItemEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("item");

    private final ICrateItemFactory           renderer;
    private final CrateItemEditorUIController controller;

    public CrateItemEditorExtension(ICrateItemFactory renderer, CrateItemEditorUIController controller) {
        this.renderer = renderer;
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator backToCrateSettings,
                                 int slot) {

        return MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(this.renderer.renderCrateIcon(crate)
                    .hideAllComponents()
                    .localized(CrateEditorLang.UI_INVENTORY_CRATE_OPTIONS_BUTTON_ITEM)
                )
                .action(context -> this.click(context, crate, hook, backToCrateSettings))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Crate crate, CrateEditorHook hook,
                       BackwardNavigator backToCrateSettings) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, crate.id(), hook, backToCrateSettings);
    }
}
