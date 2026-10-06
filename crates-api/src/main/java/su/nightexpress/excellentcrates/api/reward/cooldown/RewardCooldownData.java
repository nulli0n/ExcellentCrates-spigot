package su.nightexpress.excellentcrates.api.reward.cooldown;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.OwnableData;

@NullMarked
public interface RewardCooldownData extends OwnableData<UUID, Identifier> {

    boolean isExpired();

    boolean isExpirationAllowed();

    String getRewardIdString();

    boolean isPermanent();

    void setPermanent(boolean permanent);

    long getExpirationTimestamp();

    void setExpirationTimestamp(long cooldownTimestamp);
}
