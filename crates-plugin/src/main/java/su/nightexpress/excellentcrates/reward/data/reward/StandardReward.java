package su.nightexpress.excellentcrates.reward.data.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ImmutableEntityComponentContainer;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardBase;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public class StandardReward implements Reward {

    private final RewardId      id;
    private final RewardBase    base;
    private final RewardPreview preview;

    private final ImmutableEntityComponentContainer<RewardComponent> components;

    public StandardReward(RewardId id, StandardRewardBuilder builder) {
        this.id = id;
        this.base = builder.base;
        this.preview = builder.preview;
        this.components = new ImmutableEntityComponentContainer<>(builder.components);
    }

    @Override
    public ImmutableEntityComponentContainer<RewardComponent> getComponents() {
        return this.components;
    }

    @Override
    public RewardId getId() {
        return this.id;
    }

    @Override
    public RewardBase getBase() {
        return this.base;
    }

    @Override
    public RewardPreview getPreview() {
        return this.preview;
    }
}
