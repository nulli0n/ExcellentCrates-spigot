package su.nightexpress.excellentcrates.api.preview;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface PreviewContext {

    Crate crate();

    BackwardNavigator backwardNavigator();
}
