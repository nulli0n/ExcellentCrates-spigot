package su.nightexpress.excellentcrates.api.common.limit;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface LimitOptions {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    int getAmount();

    void setAmount(int amount);

    boolean isMoreStrict(LimitOptions other);
}
