package su.nightexpress.excellentcrates.api.reward.broadcast;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;

@NullMarked
public interface RewardBroadcastComponent extends RewardComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
