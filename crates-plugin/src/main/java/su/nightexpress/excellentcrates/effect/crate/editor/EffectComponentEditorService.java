package su.nightexpress.excellentcrates.effect.crate.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.excellentcrates.effect.lang.EffectsLang;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class EffectComponentEditorService {

    private ActionResult editComponent(CrateEditorHook hook,
                                       Function<EffectComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            EffectComponent component = crate.getComponentOrNull(CrateComponentKeys.EFFECT);
            if (component == null) {
                return ActionResult.fail(EffectsLang.GENERIC_NO_EFFECT_COMPONENT);
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

    public ActionResult setComponentProfileKey(CrateEditorHook hook, AdaptedKey profileKey) {
        return this.editComponent(hook, component -> {
            component.setProfileKey(profileKey);
            return ActionResult.ok();
        });
    }
}
