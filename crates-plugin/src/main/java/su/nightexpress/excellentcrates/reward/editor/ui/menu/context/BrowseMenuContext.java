package su.nightexpress.excellentcrates.reward.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.EditorPreferences;

@NullMarked
public record BrowseMenuContext(EditorPreferences preferences,
                                BackwardNavigator backwardNavigator) implements BackwardSupport {

}
