package su.nightexpress.engine.lazy;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public final class MutableLazy<T> implements Lazy<T> {

    @Nullable
    private T value;

    public void set(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        if (this.value == null) {
            throw new IllegalStateException("Lazy dependency is not yet initialized.");
        }
        return this.value;
    }
}