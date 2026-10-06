package su.nightexpress.excellentcrates.preview;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;

@NullMarked
public record DefaultPreviewContext(Crate crate, BackwardNavigator backwardNavigator) implements PreviewContext {

}
