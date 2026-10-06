package su.nightexpress.excellentcrates.reward.feature.cooldown.db;

import java.time.Duration;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.sql.CachedDataService;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;

@NullMarked
public class RewardCooldownCachedDataService extends CachedDataService<UUID, Identifier, RewardCooldownData> {

    public RewardCooldownCachedDataService(RewardCooldownSQLRepository repository, Duration cacheTtl) {
        super(repository, cacheTtl);
    }

    @Override
    public RewardCooldownData createDefaultData(UUID parentId, Identifier key) {
        return new DefaultRewardCooldownData(parentId, key);
    }
}
