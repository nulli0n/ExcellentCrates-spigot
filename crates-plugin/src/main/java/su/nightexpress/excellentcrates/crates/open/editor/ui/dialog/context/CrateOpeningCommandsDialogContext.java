package su.nightexpress.excellentcrates.crates.open.editor.ui.dialog.context;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public record CrateOpeningCommandsDialogContext(List<String> currentCommands,
                                                CrateEditorHook hook) {

}
