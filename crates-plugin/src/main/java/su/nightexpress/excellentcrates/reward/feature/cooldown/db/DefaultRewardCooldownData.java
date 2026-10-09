package su.nightexpress.excellentcrates.reward.feature.cooldown.db;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class DefaultRewardCooldownData implements RewardCooldownData {

    private final UUID     playerId;
    private final RewardId rewardId;

    private boolean permanent;
    private long    expirationTimestamp;

    public DefaultRewardCooldownData(UUID playerId, RewardId rewardId) {
        this(playerId, rewardId, false, 0L);
    }

    public DefaultRewardCooldownData(UUID playerId, RewardId rewardId, boolean permanent, long expirationTimestamp) {
        this.playerId = playerId;
        this.rewardId = rewardId;
        this.permanent = permanent;
        this.expirationTimestamp = expirationTimestamp;
    }

    public boolean isExpired() {
        return this.expirationTimestamp >= 0L && TimeUtil.isPassed(this.expirationTimestamp);
    }

    public boolean isExpirationAllowed() {
        return this.expirationTimestamp >= 0L;
    }

    @Override
    public UUID getParentId() {
        return this.playerId;
    }

    @Override
    public RewardId getKey() {
        return this.rewardId;
    }

    public String getRewardIdString() {
        return this.rewardId.rewardId().value();
    }

    public String getCrateIdString() {
        return this.rewardId.crateId().value();
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public long getExpirationTimestamp() {
        return expirationTimestamp;
    }

    public void setExpirationTimestamp(long cooldownTimestamp) {
        this.expirationTimestamp = cooldownTimestamp;
    }
}
