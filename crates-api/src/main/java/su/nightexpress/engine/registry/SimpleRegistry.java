package su.nightexpress.engine.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class SimpleRegistry<T> implements TinyRegistry<T> {

    private final List<T> entries;

    private boolean locked;

    public SimpleRegistry() {
        this.entries = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean isLocked() {
        return this.locked;
    }

    @Override
    public void register(T entry) {
        if (this.locked) {
            throw new IllegalStateException("Registry is locked, cannot register new entries.");
        }
        this.entries.add(entry);
    }

    @Override
    public void registerAll(Collection<T> entries) {
        if (this.locked) {
            throw new IllegalStateException("Registry is locked, cannot register new entries.");
        }
        this.entries.addAll(entries);
    }

    @Override
    public List<T> getEntries() {
        return Collections.unmodifiableList(this.entries);
    }

    @Override
    public void clear() {
        if (this.locked) {
            throw new IllegalStateException("Registry is locked, cannot clear entries.");
        }
        this.entries.clear();
    }

    @Override
    public void lock() {
        this.locked = true;
    }
}
