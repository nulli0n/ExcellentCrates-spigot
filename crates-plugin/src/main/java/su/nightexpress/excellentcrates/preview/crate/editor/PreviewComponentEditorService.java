package su.nightexpress.excellentcrates.preview.crate.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.preview.crate.PreviewComponent;
import su.nightexpress.excellentcrates.preview.crate.editor.lang.PreviewComponentLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class PreviewComponentEditorService {

    private ActionResult editComponent(CrateEditorHook hook,
                                       Function<PreviewComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            PreviewComponent component = crate.getComponentOrNull(CrateComponentKeys.PREVIEW);
            if (component == null) {
                return ActionResult.fail(PreviewComponentLang.GENERIC_NO_PREVIEW_COMPONENT);
            }

            return editor.apply(component);
        });
    }

    public ActionResult setComponentState(CrateEditorHook hook, boolean state) {
        return this.editComponent(hook, component -> {
            component.setEnabled(state);
            return ActionResult.ok();
        });
    }

    public ActionResult setComponentPreviewKey(CrateEditorHook hook, AdaptedKey previewKey) {
        return this.editComponent(hook, component -> {
            component.setPreviewKey(previewKey);
            return ActionResult.ok();
        });
    }
}
