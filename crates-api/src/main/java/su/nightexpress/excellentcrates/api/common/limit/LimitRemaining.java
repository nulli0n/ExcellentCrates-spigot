package su.nightexpress.excellentcrates.api.common.limit;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface LimitRemaining {

    boolean isExhausted();

    boolean isUnlimited();

    int getRemaining();
}
