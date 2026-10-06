package su.nightexpress.engine.cache;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.sql.OwnableData;

@NullMarked
public class ExpiringCache<P, K, V extends OwnableData<P, K>> {

    private final Map<P, Map<K, CacheEntry<V>>> cacheMap = new ConcurrentHashMap<>();

    private final Duration timeToLive;

    public ExpiringCache(Duration timeToLive) {
        this.timeToLive = timeToLive;
    }

    public void clear() {
        this.cacheMap.clear();
    }

    public void putPermanent(V value) {
        this.put(value, new CacheEntry<V>(value, -1L));
    }

    public void putTemporary(V value) {
        long expiresAt = System.currentTimeMillis() + this.timeToLive.toMillis();
        this.put(value, new CacheEntry<V>(value, expiresAt));
    }

    private void put(V value, CacheEntry<V> entry) {
        this.cacheMap.computeIfAbsent(value.getParentId(), p -> new ConcurrentHashMap<>()).put(value.getKey(), entry);
    }

    public List<V> getAllByParent(P parentId) {
        Map<K, CacheEntry<V>> parentMap = this.cacheMap.get(parentId);
        if (parentMap == null) return List.of();

        parentMap.values().removeIf(CacheEntry::isExpired);
        if (parentMap.isEmpty()) {
            this.cacheMap.remove(parentId);
        }

        return parentMap.values().stream().map(CacheEntry::payload).toList();
    }

    public List<V> getAllByKey(K key) {
        List<V> list = new ArrayList<>();

        for (P parentKey : this.cacheMap.keySet()) {
            this.getAllByParent(parentKey).stream()
                .filter(data -> data.getKey().equals(key))
                .forEach(list::add);
        }

        return list;
    }

    public Optional<V> get(P parentId, K key) {
        return this.getEntry(parentId, key).map(CacheEntry::payload);
    }

    private Optional<CacheEntry<V>> getEntry(P parentId, K key) {
        Map<K, CacheEntry<V>> parentMap = this.cacheMap.get(parentId);
        if (parentMap == null) {
            return Optional.empty();
        }

        CacheEntry<V> entry = parentMap.get(key);
        if (entry == null) {
            return Optional.empty();
        }

        if (entry.isExpired()) {
            parentMap.remove(key, entry);
            if (parentMap.isEmpty()) {
                this.cacheMap.remove(parentId, parentMap);
            }
            return Optional.empty();
        }

        return Optional.of(entry);
    }

    public void remove(V data) {
        this.remove(data.getParentId(), data.getKey());
    }

    public void remove(P parentId, K key) {
        Map<K, CacheEntry<V>> parentMap = this.cacheMap.get(parentId);
        if (parentMap != null) {
            parentMap.remove(key);
            if (parentMap.isEmpty()) {
                this.cacheMap.remove(parentId, parentMap);
            }
        }
    }

    public void removeAllForParent(P parentId) {
        this.cacheMap.remove(parentId);
    }

    /**
     * Updates the payload of an existing cache entry while perfectly preserving
     * its current Time-To-Live (TTL) strategy and expiration timestamp.
     * 
     * @return true if the entry was present and updated, false otherwise.
     */
    public boolean updateIfPresent(V value) {
        P parentId = value.getParentId();

        Map<K, CacheEntry<V>> parentMap = this.cacheMap.get(parentId);
        if (parentMap == null) return false;

        K key = value.getKey();

        CacheEntry<V> existing = this.getEntry(parentId, key).orElse(null);
        if (existing == null) return false;

        // Inherit the exact expiration timestamp from the existing entry
        parentMap.put(key, new CacheEntry<>(value, existing.expiresAtEpoch()));
        return true;
    }

    public boolean contains(P parentId, K key) {
        return this.get(parentId, key).isPresent();
    }
}