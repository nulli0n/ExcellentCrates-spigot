package su.nightexpress.engine.sql;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class DataStateTracker<P, K> {

    private final Map<P, Map<K, State>> stateMap;

    public enum State {
        DIRTY,
        REMOVED
    }

    public record Snapshot<P, K>(Map<P, Set<K>> dirtyKeys,
                                 Map<P, Set<K>> removedKeys) {
    }

    public DataStateTracker() {
        this.stateMap = new ConcurrentHashMap<>();
    }

    public void markDirty(P parentId, K key) {
        this.mark(parentId, key, State.DIRTY);
    }

    public void markRemoved(P parentId, K key) {
        this.mark(parentId, key, State.REMOVED);
    }

    private void mark(P parentId, K key, State state) {
        this.stateMap.computeIfAbsent(parentId, p -> new ConcurrentHashMap<>()).put(key, state);
    }

    public void clearState(P parentId, K key) {
        Map<K, State> childMap = this.stateMap.get(parentId);
        if (childMap != null) {
            childMap.remove(key);
            if (childMap.isEmpty()) {
                this.stateMap.remove(parentId, childMap);
            }
        }
    }

    /**
     * Safely flushes the tracked states into a static snapshot for the auto-saver.
     * Uses lock-free atomic removal to ensure concurrent state changes are never lost.
     */
    public Snapshot<P, K> flush() {
        Map<P, Set<K>> dirty = new HashMap<>();
        Map<P, Set<K>> removed = new HashMap<>();

        this.stateMap.forEach((parentId, childMap) -> {
            childMap.forEach((key, state) -> {
                // Atomic removal: only extracts the element if the state hasn't been overwritten 
                // by another thread a millisecond ago.
                if (childMap.remove(key, state)) {
                    if (state == State.DIRTY) {
                        dirty.computeIfAbsent(parentId, p -> new HashSet<>()).add(key);
                    }
                    else if (state == State.REMOVED) {
                        removed.computeIfAbsent(parentId, p -> new HashSet<>()).add(key);
                    }
                }
            });

            // Discarding parent mappings that are no longer tracking children
            if (childMap.isEmpty()) {
                this.stateMap.remove(parentId, childMap);
            }
        });

        return new Snapshot<>(dirty, removed);
    }
}