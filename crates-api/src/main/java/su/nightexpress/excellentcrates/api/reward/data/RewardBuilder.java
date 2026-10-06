package su.nightexpress.excellentcrates.api.reward.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.EntityComponentKey;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;

@NullMarked
public interface RewardBuilder {

    Reward build();

    RewardBuilder preview(RewardPreview preview);

    <T extends RewardComponent> RewardBuilder component(EntityComponentKey<T> key, T component);
}
