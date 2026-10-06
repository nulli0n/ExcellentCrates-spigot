package su.nightexpress.excellentcrates.crates.cooldown.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateCooldownsSettingsDialogContext(CooldownType type,
                                                  CooldownSnapshot snapshot,
                                                  CrateEditorHook hook) {

}
