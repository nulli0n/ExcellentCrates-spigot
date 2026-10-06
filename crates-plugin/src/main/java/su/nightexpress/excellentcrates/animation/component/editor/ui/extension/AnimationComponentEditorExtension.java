package su.nightexpress.excellentcrates.animation.component.editor.ui.extension;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIController;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorExtension;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class AnimationComponentEditorExtension implements CrateEditorExtension {

    private static final Identifier ID = new Identifier("animation");

    private final AnimationComponentEditorUIController uiController;

    public AnimationComponentEditorExtension(AnimationComponentEditorUIController uiController) {
        this.uiController = uiController;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MenuItem createButton(Crate crate, CrateEditorHook hook, BackwardNavigator navigator, int slot) {
        AnimationComponent component = crate.getComponentOrNull(CrateComponentKeys.ANIMATION);
        boolean enabled = component != null && component.isEnabled();

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.GLOW_ITEM_FRAME)
                    .hideAllComponents()
                    .localized(AnimationsLang.EDITOR_UI_EXTENSION_BUTTON)
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
