package su.nightexpress.excellentcrates.api.key.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.key.CrateKey;

@NullMarked
@FunctionalInterface
public interface KeyEditorHook {

    ActionResult modify(Function<CrateKey, ActionResult> action);
}
