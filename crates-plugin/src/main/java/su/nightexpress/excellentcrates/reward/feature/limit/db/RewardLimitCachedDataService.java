package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.data.DataCacheService;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public class RewardLimitCachedDataService extends DataCacheService<UUID, RewardId, RewardLimitData> {

    public RewardLimitCachedDataService(RewardLimitSQLRepository repository,
                                        Duration cacheTTL) {
        super(repository, cacheTTL);
    }

    @Override
    public RewardLimitData createDefaultData(UUID parentId, RewardId key) {
        return new RewardLimitData(parentId, key);
    }
}
