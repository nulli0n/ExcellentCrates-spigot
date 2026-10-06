package su.nightexpress.excellentcrates.reward.grant.pipeline;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.PipelineExecutor;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.CrateRewardsPipelineComponent;
import su.nightexpress.excellentcrates.api.crate.pipeline.component.PipelineComponentKeys;
import su.nightexpress.excellentcrates.api.crate.pipeline.context.PipelineContext;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.reward.grant.RewardGrantService;

@NullMarked
public class RewardGrantPipelineExecutor implements PipelineExecutor {

    private final RewardGrantService grantService;

    public RewardGrantPipelineExecutor(RewardGrantService grantService) {
        this.grantService = grantService;
    }

    @Override
    public void execute(Player player, Crate crate, PipelineContext context) {
        CrateRewardsPipelineComponent rewardsComponent = context.getComponentOrNull(
            PipelineComponentKeys.REWARDS
        );
        if (rewardsComponent == null) return;

        List<PipelineReward> rewards = rewardsComponent.getTargetRewards();
        rewards.forEach(pipelineReward -> {
            this.grantService.giveReward(player, crate, pipelineReward.getGranted());
        });
    }
}
