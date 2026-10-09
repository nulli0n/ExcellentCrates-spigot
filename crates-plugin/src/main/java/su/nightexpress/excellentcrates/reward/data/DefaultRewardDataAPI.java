package su.nightexpress.excellentcrates.reward.data;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.RewardDataAPI;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public class DefaultRewardDataAPI implements RewardDataAPI {

    private final TinyRegistry<RewardDataExtension> extensions;
    private final RewardDataService                 dataService;

    public DefaultRewardDataAPI(TinyRegistry<RewardDataExtension> extensions, RewardDataService dataService) {
        this.extensions = extensions;
        this.dataService = dataService;
    }

    @Override
    public void registerExtension(RewardDataExtension extension) {
        this.extensions.register(extension);
    }

    @Override
    public void loadRewards() {
        dataService.loadRewards();
    }

    @Override
    public void loadReward(Reward reward) {
        dataService.loadReward(reward);
    }

    @Override
    public void unloadRewards() {
        dataService.unloadRewards();
    }

    @Override
    public void unloadReward(Reward reward) {
        dataService.unloadReward(reward);
    }

    @Override
    public Reward createReward(RewardId id, Consumer<RewardBuilder> onBuild, Consumer<Reward> onCreated) {
        return dataService.createReward(id, onBuild, onCreated);
    }

    @Override
    public void saveReward(Reward reward) {
        dataService.saveReward(reward);
    }

    @Override
    public @Nullable Reward getReward(RewardId id) {
        return dataService.getReward(id);
    }

    @Override
    public boolean hasReward(RewardId id) {
        return dataService.hasReward(id);
    }

    @Override
    public void deleteReward(Reward reward) {
        dataService.deleteReward(reward);
    }

    @Override
    public void markDirty(Reward reward) {
        dataService.markDirty(reward);
    }

    @Override
    public void saveDirty() {
        dataService.saveDirty();
    }

    @Override
    public void saveAll() {
        dataService.saveAll();
    }
}
