package su.nightexpress.excellentcrates.api.reward.cooldown;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.sql.OwnableData;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public interface RewardCooldownData extends OwnableData<UUID, RewardId> {

    boolean isExpired();

    boolean isExpirationAllowed();

    String getRewardIdString();

    String getCrateIdString();

    boolean isPermanent();

    void setPermanent(boolean permanent);

    long getExpirationTimestamp();

    void setExpirationTimestamp(long cooldownTimestamp);
}
