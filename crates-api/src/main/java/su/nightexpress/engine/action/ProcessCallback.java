package su.nightexpress.engine.action;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ProcessCallback<T> {

    /**
     * Called when the process successfully completes and provides a result.
     */
    void proceed(@Nullable T result);

    /**
     * Called when the process is canceled.
     */
    void cancel();
}