package su.nightexpress.excellentcrates.reward.feature.limit.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.sql.CachedDataService;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public class RewardLimitCachedDataService extends CachedDataService<UUID, RewardId, RewardLimitData> {

    public RewardLimitCachedDataService(RewardLimitSQLRepository repository,
                                        Duration cacheTTL) {
        super(repository, cacheTTL);
    }

    @Override
    public RewardLimitData createDefaultData(UUID parentId, RewardId key) {
        return new RewardLimitData(parentId, key);
    }
}
