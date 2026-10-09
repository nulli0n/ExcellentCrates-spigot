package su.nightexpress.excellentcrates.reward.evaluation.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class RewardEvaluationPlaceholder implements RewardPlaceholder {

    private final RewardEvaluationService evaluationService;

    public RewardEvaluationPlaceholder(RewardEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public Consumer<Builder> applyInCrate(Crate crate, Reward reward, @Nullable Player player) {
        return ctx -> {
            ctx.with(SharedPlaceholders.REWARD_ROLL_CHANCE, () -> {
                return NumberUtil.format(evaluationService.calculateRollChance(crate, reward));
            });
        };
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return PlaceholderApplier.empty();
    }
}
