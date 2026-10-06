package su.nightexpress.excellentcrates.keys.display.editor;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.key.editor.KeyEditorHook;

@NullMarked
public class KeyDisplayEditorService {

    public ActionResult setDisplayName(KeyEditorHook hook, String name) {
        return hook.modify(key -> {
            key.getDisplay().setName(name);

            return ActionResult.ok();
        });
    }

    public ActionResult setDisplayLore(KeyEditorHook hook, List<String> lore) {
        return hook.modify(key -> {
            key.getDisplay().setLore(lore);

            return ActionResult.ok();
        });
    }
}
