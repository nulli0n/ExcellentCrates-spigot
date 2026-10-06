package su.nightexpress.excellentcrates.keys.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record KeyBrowseMenuContext(BackwardNavigator backwardNavigator) implements BackwardSupport {

}
