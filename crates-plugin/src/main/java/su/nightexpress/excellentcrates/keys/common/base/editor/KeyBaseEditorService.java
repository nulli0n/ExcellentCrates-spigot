package su.nightexpress.excellentcrates.keys.common.base.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public class KeyBaseEditorService {

    public ActionResult setVirtualState(KeyEditorHook hook, boolean state) {
        return hook.modify(key -> {
            key.getBase().setVirtual(state);

            return ActionResult.ok();
        });
    }
}
