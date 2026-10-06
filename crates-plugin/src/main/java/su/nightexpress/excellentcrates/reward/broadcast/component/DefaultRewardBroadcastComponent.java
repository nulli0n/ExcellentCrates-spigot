package su.nightexpress.excellentcrates.reward.broadcast.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;

@NullMarked
public class DefaultRewardBroadcastComponent implements RewardBroadcastComponent {

    private boolean enabled;

    public DefaultRewardBroadcastComponent(boolean enabled) {
        this.enabled = enabled;
    }

    public static DefaultRewardBroadcastComponent createDefault() {
        return new DefaultRewardBroadcastComponent(false);
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
