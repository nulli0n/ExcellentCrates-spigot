package su.nightexpress.excellentcrates.keys.storage.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.data.DataCacheService;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.keys.storage.model.StoredKey;

@NullMarked
public class KeyStorageCachedDataService extends DataCacheService<UUID, Identifier, StoredKey> {

    public KeyStorageCachedDataService(KeyStorageSQLRepository repository, Duration cacheTTL) {
        super(repository, cacheTTL);
    }

    @Override
    public StoredKey createDefaultData(UUID parentId, Identifier key) {
        return new StoredKey(parentId, key);
    }
}
