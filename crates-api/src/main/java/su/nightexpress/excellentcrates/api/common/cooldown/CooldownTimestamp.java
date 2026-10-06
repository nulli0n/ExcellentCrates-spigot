package su.nightexpress.excellentcrates.api.common.cooldown;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.TimeUtil;

@NullMarked
public record CooldownTimestamp(boolean isPermanent, long expirationTimestamp) {

    public static CooldownTimestamp permanent() {
        return new CooldownTimestamp(true, -1);
    }

    public static CooldownTimestamp temporal(long expirationTimestamp) {
        return new CooldownTimestamp(false, Math.max(0, expirationTimestamp));
    }

    public boolean isExpired() {
        return !this.isPermanent && TimeUtil.isPassed(this.expirationTimestamp);
    }
}
