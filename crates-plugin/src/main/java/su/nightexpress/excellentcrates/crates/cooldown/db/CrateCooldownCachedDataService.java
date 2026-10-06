package su.nightexpress.excellentcrates.crates.cooldown.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.CachedDataService;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;

@NullMarked
public class CrateCooldownCachedDataService extends CachedDataService<UUID, Identifier, CrateCooldownData> {

    public CrateCooldownCachedDataService(CrateCooldownSQLRepository repository, Duration cacheTtl) {
        super(repository, cacheTtl);
    }

    @Override
    public CrateCooldownData createDefaultData(UUID parentId, Identifier key) {
        return new DefaultCrateCooldownData(parentId, key);
    }
}
