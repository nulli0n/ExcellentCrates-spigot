package su.nightexpress.excellentcrates.api.reward.evaluation;

import java.util.List;
import java.util.Optional;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;

@NullMarked
public interface RewardEvaluator {

    @Nullable
    CrateRewardsComponent getRewardsComponent(Crate crate);

    List<Reward> getAvailableRewards(Crate crate, Player player);

    int getRequiredRewards(Crate crate);

    Optional<Reward> rollReward(Crate crate, Player player);

    Optional<Reward> rollReward(Crate crate, Rarity rarity, Player player);

    boolean isRewardAvailable(Crate crate, Reward reward, Player player);

    double calculateRollChance(Crate crate, Reward reward);

    double calculateProbability(Crate crate, Reward reward);

    double calculateRewardProbability(Crate crate, Identifier rewardId);
}
