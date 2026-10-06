package su.nightexpress.excellentcrates.api.reward.selectable;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface SelectableRewardsComponent extends CrateComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);
}
