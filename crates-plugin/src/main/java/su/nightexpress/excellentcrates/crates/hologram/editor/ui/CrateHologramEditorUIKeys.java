package su.nightexpress.excellentcrates.crates.hologram.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramOffsetDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramTextDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.context.CrateHologramOptionsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class CrateHologramEditorUIKeys {

    public static final MenuKey<CrateHologramOptionsMenuContext> MENU_OPTIONS = MenuKey.of(
        "crate.holograms.editor.options"
    );

    public static final DialogKey<CrateHologramTextDialogContext> DIALOG_HOLOGRAM_TEXT = new DialogKey<>(
        "crate.holograms.editor.text"
    );

    public static final DialogKey<CrateHologramOffsetDialogContext> DIALOG_HOLOGRAM_OFFSET = new DialogKey<>(
        "crate.holograms.editor.offset"
    );

    private CrateHologramEditorUIKeys() {
    }
}
