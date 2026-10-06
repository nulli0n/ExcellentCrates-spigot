package su.nightexpress.excellentcrates.reward.quota.pipeline;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.reward.quota.RewardQuotaService;

@NullMarked
public class RewardQuotaApplyExecutor implements PipelineExecutor {

    private final RewardQuotaService quotaService;

    public RewardQuotaApplyExecutor(RewardQuotaService quotaService) {
        this.quotaService = quotaService;
    }

    @Override
    public void execute(Player player, Crate crate, PipelineContext context) {
        context.getComponent(PipelineComponentKeys.REWARDS).ifPresent(rewards -> {
            rewards.getTargetRewards().forEach(pipeReward -> {
                this.quotaService.applyQuotas(player, crate, pipeReward.getGranted());
            });
        });
    }
}
