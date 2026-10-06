package su.nightexpress.excellentcrates.rarity.reward.component.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.dialog.context.RarityComponentSelectionDialogContext;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.ui.menu.context.RarityComponentMainMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RarityComponentEditorUIKeys {

    public static final MenuKey<RarityComponentMainMenuContext> MENU_MAIN = MenuKey.of(
        "rarity.reward.component.editor.main"
    );

    public static final DialogKey<RarityComponentSelectionDialogContext> DIALOG_SELECTION = new DialogKey<>(
        "rarity.reward.component.selection"
    );

    private RarityComponentEditorUIKeys() {
    }
}
