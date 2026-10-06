package su.nightexpress.excellentcrates.core.common.limit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;

@NullMarked
public class DefaultLimitOptions implements LimitOptions {

    private boolean enabled;
    private int     amount;

    public DefaultLimitOptions(boolean enabled, int amount) {
        this.enabled = enabled;
        this.amount = amount;
    }

    public static DefaultLimitOptions createDefault() {
        return new DefaultLimitOptions(false, 0);
    }

    @Override
    public boolean isMoreStrict(LimitOptions other) {
        return this.enabled && (!other.isEnabled() || this.amount < other.getAmount());
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(int amount) {
        this.amount = amount;
    }

}
