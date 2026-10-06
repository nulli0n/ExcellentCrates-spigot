package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.util.UUID;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.OwnableData;

public class RewardLimitData implements OwnableData<UUID, Identifier> {

    private final UUID       playerId;
    private final Identifier rewardId;

    private int rolls;

    public RewardLimitData(UUID playerId, Identifier rewardId) {
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
    public Identifier getKey() {
        return this.getRewardId();
    }

    public UUID getPlayerId() {
        return this.playerId;
    }

    public Identifier getRewardId() {
        return this.rewardId;
    }

    public String getRewardIdString() {
        return this.rewardId.value();
    }

    public int getRolls() {
        return this.rolls;
    }

    public void setRolls(int rolls) {
        this.rolls = Math.max(0, rolls);
    }
}
