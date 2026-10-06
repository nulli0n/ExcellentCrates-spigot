package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.CachedDataService;

@NullMarked
public class RewardLimitCachedDataService extends CachedDataService<UUID, Identifier, RewardLimitData> {

    public RewardLimitCachedDataService(RewardLimitSQLRepository repository,
                                        Duration cacheTTL) {
        super(repository, cacheTTL);
    }

    @Override
    public RewardLimitData createDefaultData(UUID parentId, Identifier key) {
        return new RewardLimitData(parentId, key);
    }
}
