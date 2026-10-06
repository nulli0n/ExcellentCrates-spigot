package su.nightexpress.excellentcrates.api.reward.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
@FunctionalInterface
public interface RewardEditorHook {

    ActionResult modify(Function<Reward, ActionResult> action);
}
