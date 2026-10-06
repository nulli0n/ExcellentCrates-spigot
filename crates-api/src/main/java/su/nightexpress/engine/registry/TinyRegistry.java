package su.nightexpress.engine.registry;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface TinyRegistry<T> extends Iterable<T> {

    @Override
    default Iterator<T> iterator() {
        return this.getEntries().iterator();
    }

    boolean isLocked();

    void register(T entry);

    void registerAll(Collection<T> entries);

    void clear();

    void lock();

    List<T> getEntries();
}
