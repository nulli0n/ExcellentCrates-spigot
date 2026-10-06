package su.nightexpress.engine.data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class DirtyTracker<T> {

    private final Set<T> dirties;

    public DirtyTracker() {
        this.dirties = ConcurrentHashMap.newKeySet();
    }

    public void add(@NonNull T object) {
        this.dirties.add(object);
    }

    public void remove(@NonNull T object) {
        this.dirties.remove(object);
    }

    public boolean has(@NonNull T object) {
        return this.dirties.contains(object);
    }

    public boolean hasAny() {
        return !this.dirties.isEmpty();
    }

    public Set<T> removeAndGetDirty() {
        Set<T> dirtyObjects = new HashSet<>(this.dirties);
        this.dirties.removeAll(dirtyObjects);
        return dirtyObjects;
    }
}
