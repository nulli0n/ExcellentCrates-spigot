package su.nightexpress.excellentcrates.reward.crate.editor;

import java.util.function.Function;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardEntry;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class RewardComponentEditorService {

    private ActionResult editComponent(CrateEditorHook hook, Function<CrateRewardsComponent, ActionResult> editor) {
        return hook.modify(crate -> {
            CrateRewardsComponent rewards = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
            if (rewards == null) {
                return ActionResult.fail(RewardsLang.ERROR_NO_REWARDS_COMPONENT);
            }

            return editor.apply(rewards);
        });
    }

    public ActionResult addExistingReward(CrateEditorHook hook, Identifier rewardId) {
        return this.editComponent(hook, rewards -> {
            CrateRewardEntry existingReward = rewards.getReward(rewardId);
            if (existingReward != null) return ActionResult.fail(RewardsLang.ERROR_REWARD_ALREADY_ADDED, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, rewardId::value)
            );

            DefaultRewardEntry reward = new DefaultRewardEntry(rewardId, 0);
            rewards.addReward(reward);

            return ActionResult.ok();
        });
    }

    public ActionResult setCrateRewardId(CrateEditorHook hook, Identifier rewardId, Identifier selectedId) {
        return this.editComponent(hook, rewards -> {
            CrateRewardEntry crateReward = rewards.getReward(rewardId);
            if (crateReward == null) return ActionResult.fail(RewardsLang.ERROR_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, rewardId::value)
            );

            CrateRewardEntry existingReward = rewards.getReward(selectedId);
            if (existingReward != null) return ActionResult.fail(RewardsLang.ERROR_REWARD_ALREADY_ADDED, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, selectedId::value)
            );

            rewards.removeReward(rewardId);
            crateReward.setRewardId(selectedId);
            rewards.addReward(crateReward);

            return ActionResult.ok();
        });
    }

    public ActionResult setCrateRewardWeight(CrateEditorHook hook, Identifier rewardId, double weight) {
        return this.editComponent(hook, rewards -> {
            CrateRewardEntry crateReward = rewards.getReward(rewardId);
            if (crateReward == null) return ActionResult.fail(RewardsLang.ERROR_REWARD_NOT_FOUND, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_VALUE, rewardId::value)
            );

            crateReward.setWeight(weight);

            return ActionResult.ok();
        });
    }

    public ActionResult setRequiredAmount(CrateEditorHook hook, int requiredAmount) {
        return this.editComponent(hook, rewards -> {
            rewards.setRequiredAmount(requiredAmount);

            return ActionResult.ok();
        });
    }
}
