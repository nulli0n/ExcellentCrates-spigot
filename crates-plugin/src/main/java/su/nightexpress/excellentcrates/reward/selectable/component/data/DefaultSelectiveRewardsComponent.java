package su.nightexpress.excellentcrates.reward.selectable.component.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.selectable.SelectableRewardsComponent;

@NullMarked
public class DefaultSelectiveRewardsComponent implements SelectableRewardsComponent {

    private boolean enabled;

    public DefaultSelectiveRewardsComponent(boolean enabled) {
        this.enabled = enabled;
    }

    public static DefaultSelectiveRewardsComponent createDefault() {
        return new DefaultSelectiveRewardsComponent(false);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
