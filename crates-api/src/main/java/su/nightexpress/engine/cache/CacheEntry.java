package su.nightexpress.engine.cache;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record CacheEntry<V>(V payload, long expiresAtEpoch) {

    public boolean isExpired() {
        return this.expiresAtEpoch != -1 && System.currentTimeMillis() >= this.expiresAtEpoch;
    }
}