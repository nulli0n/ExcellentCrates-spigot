package su.nightexpress.excellentcrates.api.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.entity.ComponentEntity;
import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;

@NullMarked
public interface Reward extends ComponentEntity<RewardComponent>, Identifiable {

    RewardPreview getPreview();
}
