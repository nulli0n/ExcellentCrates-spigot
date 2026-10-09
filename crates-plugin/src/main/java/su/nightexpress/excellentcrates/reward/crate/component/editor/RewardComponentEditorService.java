package su.nightexpress.excellentcrates.reward.crate.component.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.component.model.StandardRewardEntry;

@NullMarked
public class RewardComponentEditorService {

    private ActionResult editComponent(CrateEditorHook hook, Function<CrateRewardsComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            CrateRewardsComponent rewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
            if (rewards == null) {
                return ActionResult.fail(RewardComponentLang.ERROR_NO_REWARDS_COMPONENT);
            }

            return editor.apply(rewards);
        });
    }

    public ActionResult addReward(CrateEditorHook hook, Identifier rewardId) {
        return this.editComponent(hook, rewards -> {
            rewards.addReward(new StandardRewardEntry(rewardId));

            return ActionResult.ok();
        });
    }

    public ActionResult removeReward(CrateEditorHook hook, Identifier rewardId) {
        return this.editComponent(hook, rewards -> {
            rewards.removeReward(rewardId);

            return ActionResult.ok();
        });
    }

    public ActionResult setRequiredAmount(CrateEditorHook hook, int requiredAmount) {
        return this.editComponent(hook, rewards -> {
            rewards.setRollCount(requiredAmount);

            return ActionResult.ok();
        });
    }
}
