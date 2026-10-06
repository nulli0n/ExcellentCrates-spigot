package su.nightexpress.excellentcrates.reward.feature.limit.editor;

import java.util.function.BiFunction;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.api.common.limit.LimitSnapshot;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.reward.feature.limit.lang.RewardLimitsLang;

@NullMarked
public class RewardLimitsEditorService {

    private final RewardPlaceholders rewardPlaceholders;

    public RewardLimitsEditorService(RewardPlaceholders rewardPlaceholders) {
        this.rewardPlaceholders = rewardPlaceholders;
    }

    private ActionResult editComponent(RewardEditorHook hook,
                                       BiFunction<Reward, RewardLimitComponent, ActionResult> editor) {
        return hook.modify(reward -> {
            RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
            if (limit == null) {
                return ActionResult.fail(RewardLimitsLang.GENERIC_NO_LIMIT_COMPONENT, ctx -> ctx
                    .apply(this.rewardPlaceholders.basePlaceholders(reward))
                );
            }

            return editor.apply(reward, limit);
        });
    }

    public ActionResult setLimitsState(RewardEditorHook hook, boolean newState) {
        return editComponent(hook, (reward, limit) -> {
            limit.setEnabled(newState);
            return ActionResult.ok();
        });
    }

    public ActionResult setLimitOptions(RewardEditorHook hook, LimitType type, LimitSnapshot newSnapshot) {
        return editComponent(hook, (reward, limit) -> {

            LimitOptions options = switch (type) {
                case GLOBAL -> limit.getGlobalOptions();
                case INDIVIDUAL -> limit.getIndividualOptions();
            };

            options.setEnabled(newSnapshot.enabled());
            options.setAmount(newSnapshot.amount());

            return ActionResult.ok();
        });
    }

    public ActionResult setAlternativeEnabled(RewardEditorHook hook, boolean enabled) {
        return editComponent(hook, (reward, limit) -> {
            limit.setAlternativeEnabled(enabled);
            return ActionResult.ok();
        });
    }

    public ActionResult setAlternativeRewardId(RewardEditorHook hook, Identifier rewardId) {
        return editComponent(hook, (reward, limit) -> {
            limit.setAlternativeRewardId(rewardId);
            return ActionResult.ok();
        });
    }
}
