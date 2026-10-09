package su.nightexpress.excellentcrates.api.reward.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public interface CrateRewardEntry {

    Identifier getRewardId();

    void setRewardId(Identifier rewardId);
}
