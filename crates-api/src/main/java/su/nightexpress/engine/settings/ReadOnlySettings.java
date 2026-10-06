package su.nightexpress.engine.settings;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ReadOnlySettings<T> {

    T get();
}
