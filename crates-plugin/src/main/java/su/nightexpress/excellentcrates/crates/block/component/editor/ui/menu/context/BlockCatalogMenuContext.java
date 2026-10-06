package su.nightexpress.excellentcrates.crates.block.component.editor.ui.menu.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;

@NullMarked
public record BlockCatalogMenuContext(Identifier crateId,
                                      BackwardNavigator backwardNavigator) implements BackwardSupport {

}
