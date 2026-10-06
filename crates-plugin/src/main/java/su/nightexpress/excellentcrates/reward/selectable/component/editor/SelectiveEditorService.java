package su.nightexpress.excellentcrates.reward.selectable.component.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;
import su.nightexpress.excellentcrates.reward.selectable.lang.SelectableLang;

@NullMarked
public class SelectiveEditorService {

    private ActionResult modifyComponent(CrateEditorHook hook,
                                         Function<SelectableRewardsComponent, ActionResult> modifier) {
        return hook.modify(crate -> {
            SelectableRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.SELECTABLE_REWARDS);
            if (component == null) {
                return ActionResult.fail(SelectableLang.ERROR_NO_SELECTABLE_COMPONENT);
            }

            return modifier.apply(component);
        });
    }

    public ActionResult setComponentState(CrateEditorHook hook, boolean enabled) {
        return modifyComponent(hook, component -> {
            component.setEnabled(enabled);
            return ActionResult.ok();
        });
    }
}
