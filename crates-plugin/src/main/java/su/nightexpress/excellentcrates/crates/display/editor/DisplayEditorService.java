package su.nightexpress.excellentcrates.crates.display.editor;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateDisplay;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;

@NullMarked
public class DisplayEditorService {

    public void setDisplayName(CrateEditorHook hook, String name) {
        hook.modify(crate -> {
            CrateDisplay display = crate.getDisplay();
            display.setName(name);

            return ActionResult.ok();
        });
    }

    public void setDisplayLore(CrateEditorHook hook, List<String> lore) {
        hook.modify(crate -> {
            CrateDisplay display = crate.getDisplay();
            display.setLore(lore);

            return ActionResult.ok();
        });
    }
}
