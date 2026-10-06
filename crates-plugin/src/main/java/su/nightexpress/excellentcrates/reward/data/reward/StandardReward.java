package su.nightexpress.excellentcrates.reward.data.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ImmutableEntityComponentContainer;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;

@NullMarked
public class StandardReward implements Reward {

    private final Identifier    id;
    private final RewardPreview preview;

    private final ImmutableEntityComponentContainer<RewardComponent> components;

    public StandardReward(Identifier id, StandardRewardBuilder builder) {
        this.id = id;
        this.preview = builder.preview;
        this.components = new ImmutableEntityComponentContainer<>(builder.components);
    }

    @Override
    public ImmutableEntityComponentContainer<RewardComponent> getComponents() {
        return this.components;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RewardPreview getPreview() {
        return this.preview;
    }
}
