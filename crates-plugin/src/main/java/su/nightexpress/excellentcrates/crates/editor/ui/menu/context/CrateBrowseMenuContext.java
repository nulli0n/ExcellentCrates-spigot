package su.nightexpress.excellentcrates.crates.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record CrateBrowseMenuContext(BackwardNavigator backwardNavigator) implements BackwardSupport {

}
