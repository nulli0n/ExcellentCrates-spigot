package su.nightexpress.excellentcrates.api.common.quota;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record QuotaThreshold(int threshold, boolean isAbsent) {

    public static QuotaThreshold absent() {
        return new QuotaThreshold(0, true);
    }

    public static QuotaThreshold of(int threshold) {
        return new QuotaThreshold(threshold, false);
    }

    public boolean isAbsent() {
        return this.isAbsent;
    }

    public boolean isExhausted() {
        return !this.isAbsent && this.threshold <= 0;
    }

    public int getThreshold() {
        return this.threshold;
    }
}
