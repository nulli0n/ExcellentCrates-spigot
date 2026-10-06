package su.nightexpress.excellentcrates.core.common.limit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.limit.LimitRemaining;

@NullMarked
public record DefaultLimitRemaining(boolean isUnlimited, int remaining) implements LimitRemaining {

    public static DefaultLimitRemaining unlimited() {
        return new DefaultLimitRemaining(true, -1);
    }

    public static DefaultLimitRemaining limited(int remaining) {
        return new DefaultLimitRemaining(false, Math.max(0, remaining));
    }

    @Override
    public int getRemaining() {
        return this.remaining;
    }

    @Override
    public boolean isExhausted() {
        return !this.isUnlimited && this.remaining <= 0;
    }

}
