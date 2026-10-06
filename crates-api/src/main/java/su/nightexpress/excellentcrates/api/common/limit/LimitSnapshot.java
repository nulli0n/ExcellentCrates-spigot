package su.nightexpress.excellentcrates.api.common.limit;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record LimitSnapshot(boolean enabled, int amount) {

    public static LimitSnapshot of(LimitOptions options) {
        return new LimitSnapshot(options.isEnabled(), options.getAmount());
    }
}
