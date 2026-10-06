package su.nightexpress.excellentcrates.crates.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;

@NullMarked
public record CrateDeletionDialogContext(Identifier crateId, BackwardNavigator backwardNavigator) {

}
