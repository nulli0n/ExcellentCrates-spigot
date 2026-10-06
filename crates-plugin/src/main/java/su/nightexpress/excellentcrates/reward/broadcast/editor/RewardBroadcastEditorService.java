package su.nightexpress.excellentcrates.reward.broadcast.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.broadcast.lang.RewardBroadcastLang;

@NullMarked
public class RewardBroadcastEditorService {

    private ActionResult modifyComponent(RewardEditorHook hook,
                                         Function<RewardBroadcastComponent, ActionResult> modifier) {
        return hook.modify(reward -> {
            RewardBroadcastComponent component = reward.getComponentOrNull(RewardComponentKeys.BROADCAST);
            if (component == null) {
                return ActionResult.fail(RewardBroadcastLang.ERROR_NO_BROADCAST_COMPONENT);
            }

            return modifier.apply(component);
        });
    }

    public ActionResult setWinBroadcastEnabled(RewardEditorHook hook, boolean enabled) {
        return this.modifyComponent(hook, component -> {
            component.setEnabled(enabled);

            return ActionResult.ok();
        });
    }
}
