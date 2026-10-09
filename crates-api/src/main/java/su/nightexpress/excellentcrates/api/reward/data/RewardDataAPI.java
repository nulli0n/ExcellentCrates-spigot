package su.nightexpress.excellentcrates.api.reward.data;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public interface RewardDataAPI {

    void registerExtension(RewardDataExtension extension);

    void loadRewards();

    void loadReward(Reward reward);

    void unloadRewards();

    void unloadReward(Reward reward);

    void saveReward(Reward reward);

    Reward createReward(RewardId id, Consumer<RewardBuilder> onBuild, Consumer<Reward> onCreated);

    @Nullable
    Reward getReward(RewardId id);

    boolean hasReward(RewardId id);

    void deleteReward(Reward reward);

    void markDirty(Reward reward);

    void saveDirty();

    void saveAll();
}
