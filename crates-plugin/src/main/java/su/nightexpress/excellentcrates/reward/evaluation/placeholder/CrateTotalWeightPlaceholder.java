package su.nightexpress.excellentcrates.reward.evaluation.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.evaluation.RewardEvaluationService;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class CrateTotalWeightPlaceholder implements CratePlaceholder {

    private final RewardEvaluationService evaluationService;

    public CrateTotalWeightPlaceholder(RewardEvaluationService evaluationService) {
        this.evaluationService = evaluationService;
    }

    @Override
    public PlaceholderApplier apply(Crate crate) {
        return ctx -> {
            ctx.with(SharedPlaceholders.CRATE_TOTAL_REWARDS_WEIGHT, () -> {
                return NumberUtil.format(this.evaluationService.getTotalWeight(crate));
            });
        };
    }

    @Override
    public PlaceholderApplier apply(Crate crate, Player player) {
        return PlaceholderApplier.empty();
    }
}
