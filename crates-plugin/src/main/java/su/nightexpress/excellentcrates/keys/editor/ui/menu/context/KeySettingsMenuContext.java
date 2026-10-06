package su.nightexpress.excellentcrates.keys.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record KeySettingsMenuContext(Identifier keyId,
                                     BackwardNavigator backwardNavigator) implements BackwardSupport {

}
