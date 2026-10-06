package su.nightexpress.excellentcrates.keys.item.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public record KeyItemMainMenuContext(Identifier keyId,
                                     KeyEditorHook hook,
                                     BackwardNavigator backwardNavigator) implements BackwardSupport {

}
