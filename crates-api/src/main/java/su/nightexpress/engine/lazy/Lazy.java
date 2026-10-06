package su.nightexpress.engine.lazy;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Lazy<T> {

    T get();
}