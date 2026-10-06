package su.nightexpress.engine.sql;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.cache.ExpiringCache;

@NullMarked
public abstract class CachedDataService<P, K, V extends OwnableData<P, K>> {

    protected final SQLRepository<P, K, V> repository;
    protected final ExpiringCache<P, K, V> cache;
    protected final DataStateTracker<P, K> stateTracker;

    public CachedDataService(SQLRepository<P, K, V> repository, Duration cacheTTL) {
        this.repository = repository;
        this.cache = new ExpiringCache<>(cacheTTL);
        this.stateTracker = new DataStateTracker<>();
    }

    private void applyCacheStrategy(V data, CacheStrategy strategy) {
        switch (strategy) {
            case PERMANENT -> this.cache.putPermanent(data);
            case TEMPORARY -> this.cache.putTemporary(data);
            case NONE -> this.cache.remove(data);
        }
    }

    public abstract V createDefaultData(@NonNull P parentId, @NonNull K key);

    public CompletableFuture<List<V>> loadAndCacheAsync(P parentId, CacheStrategy strategy) {
        return this.repository.loadAllByParent(parentId).whenComplete((list, throwable) -> {
            if (throwable == null && list != null) {
                list.forEach(data -> this.applyCacheStrategy(data, strategy));
            }
        });
    }

    /**
     * Asynchronously loads and caches a specific data entry by its parent ID and key.
     * If the data is already cached, it will be returned immediately.
     * 
     * @param parentId the ID of the parent entity
     * @param key      the key of the data entry
     * @param strategy the caching strategy to apply
     * @return a CompletableFuture containing an Optional with the data entry if it exists
     */
    public CompletableFuture<Optional<V>> loadAndCacheAsync(P parentId, K key, CacheStrategy strategy) {
        if (this.cache.contains(parentId, key)) {
            return CompletableFuture.completedFuture(this.cache.get(parentId, key));
        }

        return this.repository.loadById(parentId, key).whenComplete((optionalData, throwable) -> {
            if (throwable == null && optionalData != null) {
                optionalData.ifPresent(data -> this.applyCacheStrategy(data, strategy));
            }
        });
    }

    /**
     * Asynchronously loads and caches a specific data entry by its parent ID and key.
     * If the data is already cached, it will be returned immediately.
     * If the data does not exist, it will be created using the provided default factory.
     * 
     * @param parentId the ID of the parent entity
     * @param key      the key of the data entry
     * @param strategy the caching strategy to apply
     * @return a CompletableFuture containing the data entry
     */
    public CompletableFuture<V> loadOrCreateAndCacheAsync(P parentId, K key, CacheStrategy strategy) {
        return this.loadAndCacheAsync(parentId, key, strategy).thenApply(optionalData -> {
            if (optionalData.isPresent()) {
                return optionalData.get();
            }

            V newData = this.createDefaultData(parentId, key);//defaultFactory.apply(parentId, key);
            this.applyCacheStrategy(newData, strategy);

            if (strategy != CacheStrategy.NONE) {
                this.markDirty(newData);
            }

            return newData;
        });
    }

    //

    /**
     * Updates the data of a StoredKey asynchronously.
     * If the StoredKey does not exist, it will be created using the provided constructor with temporary cache strategy.
     * Marks the StoredKey as dirty if the update is successful.
     * 
     * @param ownerId the UUID of the owner of the key
     * @param keyId   the identifier of the key
     * @param updater a function that updates the StoredKey and returns a boolean indicating success
     * @return a CompletableFuture that completes with true if the update was successful, false otherwise
     */
    public CompletableFuture<ActionResult> updateDataAndCreateIfAbsentAsync(P ownerId,
                                                                            K keyId,
                                                                            CacheStrategy strategy,
                                                                            Function<V, ActionResult> updater) {
        return this.loadOrCreateAndCacheAsync(ownerId, keyId, strategy)
            .thenApply(data -> {
                ActionResult result = updater.apply(data);
                if (result.success()) {
                    this.markDirty(data);
                }
                return result;
            });
    }

    public CompletableFuture<ActionResult> updateDataIfExistsAsync(P ownerId,
                                                                   K keyId,
                                                                   CacheStrategy strategy,
                                                                   Function<V, ActionResult> updater) {
        return this.loadAndCacheAsync(ownerId, keyId, strategy)
            .thenApply(optional -> {
                if (optional.isPresent()) {
                    V data = optional.get();
                    ActionResult result = updater.apply(data);
                    if (result.success()) {
                        this.markDirty(data);
                    }
                    return result;
                }
                return ActionResult.fail();
            });
    }

    // --- Cache Lookups ---

    public Optional<V> getCached(P parentId, K key) {
        return this.cache.get(parentId, key);
    }

    public List<V> getCached(P parentId) {
        return this.cache.getAllByParent(parentId);
    }

    public V getCachedOrCreate(P parentId, K key, /* BiFunction<P, K, V> defaultFactory, */ CacheStrategy strategy) {
        return this.getCached(parentId, key).orElseGet(() -> {
            V newData = this.createDefaultData(parentId, key);
            this.applyCacheStrategy(newData, strategy);

            if (strategy != CacheStrategy.NONE) {
                this.markDirty(newData);
            }

            return newData;
        });
    }

    public V getCachedStrict(P parentId, K key) {
        return this.getCached(parentId, key)
            .orElseThrow(() -> new IllegalStateException("Data for parent '" + parentId + "' and key '" + key +
                "' is not cached!"));
    }

    public void ingestSyncData(V data) {
        this.cache.updateIfPresent(data);

        // Ensure this newly synced data isn't queued for an immediate redundant auto-save.
        this.stateTracker.clearState(data.getParentId(), data.getKey());
    }

    /**
     * Modifies the caching strategy (TTL) for a specific parent/key pair.
     * If the data is not currently in the cache, this method safely does nothing.
     */
    public void updateCacheStrategy(P parentId, K key, CacheStrategy newStrategy) {
        this.getCached(parentId, key).ifPresent(data -> this.applyCacheStrategy(data, newStrategy));
    }

    public void updateCacheStrategy(P parentId, CacheStrategy newStrategy) {
        this.getCached(parentId).forEach(data -> this.applyCacheStrategy(data, newStrategy));
    }

    /**
     * Overloaded convenience method to modify the caching strategy natively
     * using the Identifiable contract.
     */
    public void updateCacheStrategy(V data, CacheStrategy newStrategy) {
        this.applyCacheStrategy(data, newStrategy);
    }

    public void clearCache() {
        this.cache.clear();
    }

    // --- State Management ---

    public void markDirty(V data) {
        this.markDirty(data.getParentId(), data.getKey());
    }

    public void markDirty(P parentId, K key) {
        this.stateTracker.markDirty(parentId, key);
    }

    public void markRemoved(V data) {
        this.markRemoved(data.getParentId(), data.getKey());
    }

    public void markAllRemovedByParent(P parentId) {
        this.cache.getAllByParent(parentId).forEach(data -> this.markRemoved(data));
    }

    public void markAllRemovedByKey(K key) {
        this.cache.getAllByKey(key).forEach(data -> this.markRemoved(data));
    }

    public void markRemoved(P parentId, K key) {
        this.stateTracker.markRemoved(parentId, key);
        this.cache.remove(parentId, key);
    }

    public CompletableFuture<Void> saveDirtyData() {
        DataStateTracker.Snapshot<P, K> snapshot = this.stateTracker.flush();

        Set<V> toSave = new HashSet<>();
        snapshot.dirtyKeys().forEach((parentId, keys) -> {
            for (K key : keys) {
                this.cache.get(parentId, key).ifPresent(toSave::add);
            }
        });

        Set<RemoveContext<P, K>> toRemove = new HashSet<>();
        snapshot.removedKeys().forEach((parentId, keys) -> {
            for (K key : keys) {
                toRemove.add(new RemoveContext<>(parentId, key));
            }
        });

        CompletableFuture<Void> saveFuture = toSave.isEmpty() ? CompletableFuture.completedFuture(
            null) : this.repository.upsert(toSave);

        CompletableFuture<Void> deleteFuture = toRemove.isEmpty() ? CompletableFuture.completedFuture(
            null) : this.repository.delete(toRemove);

        return CompletableFuture.allOf(saveFuture, deleteFuture);
    }
}