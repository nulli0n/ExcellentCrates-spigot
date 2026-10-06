package su.nightexpress.excellentcrates.reward.feature.cooldown.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownSnapshot;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.cooldown.lang.RewardCooldownsLang;

@NullMarked
public class RewardCooldownsEditorService {

    public ActionResult editCooldown(RewardEditorHook hook, CooldownType type, CooldownSnapshot newCooldown) {
        return hook.modify(reward -> {
            RewardCooldownComponent cooldowns = reward.getComponentOrNull(RewardComponentKeys.COOLDOWN);
            if (cooldowns == null) {
                return ActionResult.fail(RewardCooldownsLang.ERROR_NO_COOLDOWN_COMPONENT);
            }

            CooldownOptions coolown = switch (type) {
                case GLOBAL -> cooldowns.getGlobalCooldown();
                case INDIVIDUAL -> cooldowns.getIndividualCooldown();
            };

            coolown.setEnabled(newCooldown.enabled());
            coolown.setDuration(newCooldown.duration());
            coolown.setMode(newCooldown.mode());

            return ActionResult.ok();
        });
    }
}
