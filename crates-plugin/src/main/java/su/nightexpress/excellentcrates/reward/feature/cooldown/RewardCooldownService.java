package su.nightexpress.excellentcrates.reward.feature.cooldown;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.cooldown.IRewardCooldownService;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownComponent;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownData;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.reward.feature.cooldown.db.RewardCooldownCachedDataService;

@NullMarked
public class RewardCooldownService implements IRewardCooldownService {

    private static final UUID GLOBAL_ID = new UUID(0, 0);

    private final RewardCooldownCachedDataService dataService;

    public RewardCooldownService(RewardCooldownCachedDataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public CompletableFuture<List<RewardCooldownData>> loadGlobalCooldowns() {
        return this.dataService.loadAndCacheAsync(GLOBAL_ID, CacheStrategy.PERMANENT);
    }

    @Override
    public boolean isOnCooldown(Player player, Reward reward) {
        return this.isOnTemporalCooldown(player, reward) || this.isOnPermanentCooldown(player, reward);
    }

    @Override
    public boolean isOnTemporalCooldown(Player player, Reward reward) {
        CooldownTimestamp timestamp = this.getExpirationTimestamp(player, reward);
        return timestamp != null && !timestamp.isPermanent() && !timestamp.isExpired();
    }

    @Override
    public boolean isOnPermanentCooldown(Player player, Reward reward) {
        CooldownTimestamp timestamp = this.getExpirationTimestamp(player, reward);
        return timestamp != null && timestamp.isPermanent();
    }

    @Override
    public @Nullable CooldownTimestamp getExpirationTimestamp(Player player, Reward reward) {
        CooldownTimestamp globalTimestamp = this.getCachedTimestamp(reward, GLOBAL_ID);
        CooldownTimestamp playerTimestamp = this.getCachedTimestamp(reward, player.getUniqueId());

        return this.getStrictestCooldown(globalTimestamp, playerTimestamp);
    }

    private @Nullable CooldownTimestamp getCachedTimestamp(Reward reward, UUID targetId) {
        RewardCooldownData globalData = this.dataService.getCached(targetId, reward.id()).orElse(null);
        if (globalData == null || globalData.isExpired()) return null;

        if (globalData.isPermanent()) return CooldownTimestamp.permanent();

        return CooldownTimestamp.temporal(globalData.getExpirationTimestamp());
    }

    private @Nullable CooldownTimestamp getStrictestCooldown(@Nullable CooldownTimestamp... timestamps) {
        CooldownTimestamp strictest = null;

        for (CooldownTimestamp timestamp : timestamps) {
            if (timestamp == null || timestamp.isExpired()) continue;
            if (timestamp.isPermanent()) {
                return timestamp;
            }
            if (strictest == null || timestamp.expirationTimestamp() > strictest.expirationTimestamp()) {
                strictest = timestamp;
            }
        }

        return strictest;
    }

    public @Nullable RewardCooldownComponent getRewardCooldowns(Reward reward) {
        return reward.getComponentOrNull(RewardComponentKeys.COOLDOWN);
    }

    public boolean hasCooldownConfigured(Player player, Reward reward) {
        RewardCooldownComponent component = this.getRewardCooldowns(reward);
        if (component == null) return false;

        for (CooldownType type : CooldownType.values()) {
            CooldownOptions options = component.getCooldown(type);
            if (options.isEffectivelyEnabled()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void applyCooldowns(Player player, Reward reward) {
        RewardCooldownComponent cooldowns = this.getRewardCooldowns(reward);
        if (cooldowns == null) return;

        CooldownOptions globalCooldown = cooldowns.getGlobalCooldown();
        CooldownOptions individualCooldown = cooldowns.getIndividualCooldown();

        RewardId rewardId = reward.id();
        UUID playerId = player.getUniqueId();

        if (globalCooldown.isEffectivelyEnabled()) {
            RewardCooldownData data = this.dataService.getCachedOrCreate(GLOBAL_ID, rewardId, CacheStrategy.PERMANENT);
            data.setPermanent(false);
            data.setExpirationTimestamp(globalCooldown.createCooldownTimestamp());
            this.dataService.markDirty(data);
        }

        if (individualCooldown.isEffectivelyEnabled()) {
            RewardCooldownData data = this.dataService.getCachedOrCreate(playerId, rewardId, CacheStrategy.PERMANENT);
            data.setPermanent(false);
            data.setExpirationTimestamp(individualCooldown.createCooldownTimestamp());
            this.dataService.markDirty(data);
        }
    }
}
