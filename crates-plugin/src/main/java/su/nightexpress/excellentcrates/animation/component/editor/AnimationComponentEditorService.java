package su.nightexpress.excellentcrates.animation.component.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.animation.lang.AnimationsLang;
import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class AnimationComponentEditorService {

    private ActionResult editComponent(CrateEditorHook hook,
                                       Function<AnimationComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            AnimationComponent component = crate.getComponentOrNull(CrateComponentKeys.ANIMATION);
            if (component == null) {
                return ActionResult.fail(AnimationsLang.GENERIC_NO_ANIMATION_COMPONENT);
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

    public ActionResult setComponentAnimationKey(CrateEditorHook hook, AdaptedKey animationKey) {
        return this.editComponent(hook, component -> {
            component.setProfileKey(animationKey);
            return ActionResult.ok();
        });
    }
}
