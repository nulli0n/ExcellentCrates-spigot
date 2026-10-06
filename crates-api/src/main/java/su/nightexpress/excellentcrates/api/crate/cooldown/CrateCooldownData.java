package su.nightexpress.excellentcrates.api.crate.cooldown;

import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.OwnableData;

@NullMarked
public interface CrateCooldownData extends OwnableData<UUID, Identifier> {

    boolean isExpired();

    boolean isExpirationAllowed();

    String getCrateIdString();

    boolean isPermanent();

    void setPermanent(boolean permanent);

    long getExpirationTimestamp();

    void setExpirationTimestamp(long cooldownTimestamp);
}
