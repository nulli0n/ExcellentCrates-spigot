package su.nightexpress.excellentcrates.reward.data;

import java.util.Set;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.data.DirtyTracker;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardBuilder;

@NullMarked
public class RewardDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardDataService.class);

    private final RewardIOService ioService;
    private final RewardRegistry  rewards;

    private final TinyRegistry<RewardDataExtension> extensions;

    private final DirtyTracker<RewardId> dirtyTracker;

    public RewardDataService(RewardIOService ioService,
                             RewardRegistry repository,
                             TinyRegistry<RewardDataExtension> extensions) {
        this.ioService = ioService;
        this.rewards = repository;
        this.extensions = extensions;

        this.dirtyTracker = new DirtyTracker<>();
    }

    public void loadRewards() {
        this.ioService.readRewards().forEach(this::loadReward);

        LOGGER.info("Loaded {} rewards.", rewards.size());
    }

    public void loadReward(Reward reward) {
        this.rewards.register(reward);

        this.extensions.forEach(extension -> extension.onLoad(reward));
    }

    public void unloadRewards() {
        this.saveDirty();

        this.rewards.values().forEach(this::unloadReward);
    }

    public void unloadReward(Reward reward) {
        this.dirtyTracker.remove(reward.id());
        this.rewards.unregister(reward);

        this.extensions.forEach(extension -> extension.onUnload(reward));
    }

    public Reward createReward(RewardId id, Consumer<RewardBuilder> onBuild, Consumer<Reward> onCreate) {
        if (this.hasReward(id)) {
            throw new IllegalArgumentException("Reward with ID '" + id + "' already exists");
        }

        StandardRewardBuilder builder = new StandardRewardBuilder(id);

        this.extensions.forEach(extension -> extension.onBuild(builder));
        onBuild.accept(builder);

        Reward reward = builder.build();

        this.extensions.forEach(extension -> extension.onCreate(reward));
        onCreate.accept(reward);

        this.saveReward(reward);
        this.loadReward(reward);

        return reward;
    }

    public void saveReward(Reward reward) {
        this.dirtyTracker.remove(reward.id());
        this.ioService.writeReward(reward);
    }

    public @Nullable Reward getReward(RewardId id) {
        return this.rewards.get(id);
    }

    public boolean hasReward(RewardId id) {
        return this.rewards.containsKey(id);
    }

    public boolean deleteReward(Reward reward) {
        if (this.ioService.deleteRewardFile(reward)) {
            this.unloadReward(reward);

            this.extensions.forEach(extension -> extension.onDelete(reward));
            return true;
        }
        return false;
    }

    public void markDirty(Reward reward) {
        this.dirtyTracker.add(reward.id());
    }

    public void saveDirty() {
        if (!this.dirtyTracker.hasAny()) return;

        // Drain the queue safely
        Set<RewardId> toSave = this.dirtyTracker.removeAndGetDirty();

        for (RewardId id : toSave) {
            Reward reward = this.rewards.get(id);
            if (reward != null) {
                this.ioService.writeReward(reward);
            }
        }
    }

    public void saveAll() {
        for (Reward reward : this.rewards.values()) {
            this.ioService.writeReward(reward);
        }
    }
}
