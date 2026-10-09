package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.util.UUID;

import su.nightexpress.engine.sql.OwnableData;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

public class RewardLimitData implements OwnableData<UUID, RewardId> {

    private final UUID     playerId;
    private final RewardId rewardId;

    private int rolls;

    public RewardLimitData(UUID playerId, RewardId rewardId) {
        this.playerId = playerId;
        this.rewardId = rewardId;
    }

    public void reset() {
        this.setRolls(0);
    }

    public void addUses(int amount) {
        this.setRolls(this.rolls + amount);
    }

    @Override
    public UUID getParentId() {
        return this.getPlayerId();
    }

    @Override
    public RewardId getKey() {
        return this.getRewardId();
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public RewardId getRewardId() {
        return this.rewardId;
    }

    public String getRewardIdString() {
        return this.rewardId.rewardId().value();
    }

    public String getCrateIdString() {
        return this.rewardId.crateId().value();
    }

    public int getRolls() {
        return this.rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = Math.max(0, rolls);
    }
}
