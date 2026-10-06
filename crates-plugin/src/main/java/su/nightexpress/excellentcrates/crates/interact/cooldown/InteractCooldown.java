package su.nightexpress.excellentcrates.crates.interact.cooldown;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record InteractCooldown(long expiryTimestamp) {

    public boolean hasExpired() {
        return System.currentTimeMillis() >= this.expiryTimestamp;
    }
}
