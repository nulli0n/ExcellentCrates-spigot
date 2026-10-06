package su.nightexpress.excellentcrates.crates.open.editor;

import java.util.List;
import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.open.OpenActionsComponent;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.crates.open.lang.CrateOpeningLang;

@NullMarked
public class CrateOpeningEditorService {

    private final CratePlaceholders cratePlaceholders;

    public CrateOpeningEditorService(CratePlaceholders cratePlaceholders) {
        this.cratePlaceholders = cratePlaceholders;
    }

    private ActionResult editComponent(CrateEditorHook hook, Function<OpenActionsComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            OpenActionsComponent component = crate.getComponentOrNull(CrateComponentKeys.OPEN_ACTIONS);
            if (component == null) {
                return ActionResult.fail(CrateOpeningLang.EDITOR_NO_COMPONENT, ctx -> ctx
                    .apply(this.cratePlaceholders.basePlaceholders(crate))
                );
            }

            return editor.apply(component);
        });
    }

    public ActionResult setComponentState(CrateEditorHook hook, boolean state) {
        return editComponent(hook, component -> {
            component.setEnabled(state);
            return ActionResult.ok();
        });
    }

    public ActionResult setComponentCommands(CrateEditorHook hook, List<String> commands) {
        return editComponent(hook, component -> {
            component.setCommands(commands);
            return ActionResult.ok();
        });
    }
}
