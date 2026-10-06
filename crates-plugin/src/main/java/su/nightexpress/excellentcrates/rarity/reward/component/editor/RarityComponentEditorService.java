package su.nightexpress.excellentcrates.rarity.reward.component.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.rarity.reward.component.lang.RarityComponentLang;

@NullMarked
public class RarityComponentEditorService {

    private ActionResult editComponent(RewardEditorHook hook,
                                       Function<RarityComponent, ActionResult> editor) {
        return hook.modify(reward -> {
            RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
            if (component == null) {
                return ActionResult.fail(RarityComponentLang.GENERIC_NO_RARITY_COMPONENT);
            }

            return editor.apply(component);
        });
    }

    public ActionResult setComponentState(RewardEditorHook hook, boolean state) {
        return this.editComponent(hook, component -> {
            component.setEnabled(state);
            return ActionResult.ok();
        });
    }

    public ActionResult setComponentRarityId(RewardEditorHook hook, Identifier rarityId) {
        return this.editComponent(hook, component -> {
            component.setRarityId(rarityId);
            return ActionResult.ok();
        });
    }
}
