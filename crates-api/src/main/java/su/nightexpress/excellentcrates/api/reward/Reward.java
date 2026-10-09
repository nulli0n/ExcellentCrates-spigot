package su.nightexpress.excellentcrates.api.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ComponentEntity;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardBase;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public interface Reward extends ComponentEntity<RewardComponent> {

    default RewardId id() {
        return getId();
    }

    default Identifier rawId() {
        return getId().rewardId();
    }

    default double getWeight() {
        return getBase().getWeight();
    }

    RewardId getId();

    RewardBase getBase();

    RewardPreview getPreview();
}
