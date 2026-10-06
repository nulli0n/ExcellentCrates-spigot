package su.nightexpress.excellentcrates.api.animation;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.pipeline.reward.PipelineReward;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;

@NullMarked
public record AnimationContext(Player player,
                               Location location,
                               Crate crate,
                               PipelineReward reward,
                               List<Reward> availableRewards,
                               RewardPreviewAPI rewardPreview) {

}
