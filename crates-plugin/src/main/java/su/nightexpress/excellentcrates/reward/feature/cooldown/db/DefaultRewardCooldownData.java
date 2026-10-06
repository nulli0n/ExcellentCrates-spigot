package su.nightexpress.excellentcrates.reward.feature.cooldown.db;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class DefaultRewardCooldownData implements RewardCooldownData {

    private final UUID       playerId;
    private final Identifier rewardId;

    private boolean permanent;
    private long    expirationTimestamp;

    public DefaultRewardCooldownData(UUID playerId, Identifier rewardId) {
        this(playerId, rewardId, false, 0L);
    }

    public DefaultRewardCooldownData(UUID playerId, Identifier rewardId, boolean permanent, long expirationTimestamp) {
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
    public Identifier getKey() {
        return this.rewardId;
    }

    public String getRewardIdString() {
        return this.rewardId.value();
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
