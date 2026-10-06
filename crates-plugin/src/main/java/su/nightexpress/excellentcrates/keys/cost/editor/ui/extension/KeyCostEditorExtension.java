package su.nightexpress.excellentcrates.keys.cost.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.key.crate.KeyRequirementComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.cost.editor.ui.KeyCostEditorUIController;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class KeyCostEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("keys");

    private final KeyCostEditorUIController controller;

    public KeyCostEditorExtension(KeyCostEditorUIController controller) {
        this.controller = controller;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook context, BackwardNavigator navigator, int slot) {
        KeyRequirementComponent component = crate.getComponentOrNull(CrateComponentKeys.KEY_REQUIREMENT);
        int entries = component != null ? component.getKeyEntryMap().size() : 0;
        boolean state = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.TRIAL_KEY)
                    .localized(KeyCostLang.EDITOR_UI_EXTENSION_BUTTON)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(state))
                        .with(CommonPlaceholders.GENERIC_AMOUNT, () -> NumberUtil.format(entries))
                    )
                )
                .action(actionContext -> this.click(actionContext, crate, context, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(ActionContext context, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        Player player = context.getPlayer();

        this.controller.onExtensionClick(player, crate, hook, navigator);
    }
}
