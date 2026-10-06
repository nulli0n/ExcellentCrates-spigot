package su.nightexpress.excellentcrates.api.crate.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
@FunctionalInterface
public interface CrateEditorHook {

    ActionResult modify(Function<Crate, ActionResult> modifier);
}
