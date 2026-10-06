package su.nightexpress.excellentcrates.crates.cooldown.db;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;
import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public class DefaultCrateCooldownData implements CrateCooldownData {

    private final UUID       playerId;
    private final Identifier crateId;

    private boolean permanent;
    private long    expirationTimestamp;

    public DefaultCrateCooldownData(UUID playerId, Identifier crateId) {
        this(playerId, crateId, false, 0L);
    }

    public DefaultCrateCooldownData(UUID playerId, Identifier crateId, boolean permanent, long expirationTimestamp) {
        this.playerId = playerId;
        this.crateId = crateId;
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
        return this.crateId;
    }

    public String getCrateIdString() {
        return this.crateId.value();
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
