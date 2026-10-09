package su.nightexpress.excellentcrates.reward.crate.component.model;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;

@NullMarked
public class StandardRewardEntry implements CrateRewardEntry {

    private Identifier rewardId;

    public StandardRewardEntry(Identifier rewardId) {
        this.rewardId = rewardId;
    }

    @Override
    public Identifier getRewardId() {
        return rewardId;
    }

    @Override
    public void setRewardId(Identifier rewardId) {
        this.rewardId = rewardId;
    }
}
