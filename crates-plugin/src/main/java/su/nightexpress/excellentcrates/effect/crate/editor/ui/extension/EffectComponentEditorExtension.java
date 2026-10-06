package su.nightexpress.excellentcrates.effect.crate.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.effect.crate.editor.ui.EffectComponentEditorUIController;
import su.nightexpress.excellentcrates.effect.lang.EffectsLang;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class EffectComponentEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("effect.component.editor.ext");

    private final EffectComponentEditorUIController uiController;

    public EffectComponentEditorExtension(EffectComponentEditorUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot) {
        EffectComponent component = crate.getComponentOrNull(CrateComponentKeys.EFFECT);
        boolean enabled = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BLAZE_POWDER)
                    .hideAllComponents()
                    .localized(EffectsLang.EDITOR_UI_EXTENSION_BUTTON)
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(enabled))
                    )
                )
                .action(ctx -> this.click(ctx.getPlayer(), crate, hook, navigator))
                .build()
            )
            .slots(slot)
            .build();
    }

    private void click(Player player, Crate crate, CrateEditorHook hook, BackwardNavigator navigator) {
        this.uiController.onExtensionClick(player, crate, hook, navigator);
    }
}
