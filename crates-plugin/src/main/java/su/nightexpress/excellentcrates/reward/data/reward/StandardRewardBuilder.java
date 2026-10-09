package su.nightexpress.excellentcrates.reward.data.reward;

import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardBase;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public class StandardRewardBuilder implements RewardBuilder {

    private final RewardId id;

    final Map<Identifier, RewardComponent> components;

    RewardBase    base    = StandardRewardBase.createDefault();
    RewardPreview preview = StandardRewardPreview.createDefault();

    public StandardRewardBuilder(RewardId id) {
        this.id = id;
        this.components = new HashMap<>();
    }

    @Override
    public Reward build() {
        return new StandardReward(this.id, this);
    }

    @Override
    public StandardRewardBuilder base(RewardBase base) {
        this.base = base;
        return this;
    }

    @Override
    public StandardRewardBuilder preview(RewardPreview preview) {
        this.preview = preview;
        return this;
    }

    @Override
    public <T extends RewardComponent> StandardRewardBuilder component(EntityComponentKey<T> key, T component) {
        this.components.put(key.id(), component);
        return this;
    }
}
