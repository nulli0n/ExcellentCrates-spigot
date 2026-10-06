package su.nightexpress.excellentcrates.reward.feature.limit;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.IntSupplier;
import java.util.stream.Stream;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.api.common.limit.LimitRemaining;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.limit.RewardLimitComponent;
import su.nightexpress.excellentcrates.core.common.limit.DefaultLimitRemaining;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitCachedDataService;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitData;

@NullMarked
public class RewardLimitManageService {

    private static final UUID GLOBAL_ID = new UUID(0, 0);

    private final RewardLimitCachedDataService cachedDataService;

    public RewardLimitManageService(RewardLimitCachedDataService cachedDataService) {
        this.cachedDataService = cachedDataService;
    }

    public CompletableFuture<List<RewardLimitData>> loadGlobalLimits() {
        return this.cachedDataService.loadAndCacheAsync(GLOBAL_ID, CacheStrategy.PERMANENT);
    }

    public boolean isAllowedToRoll(Player player, Reward reward) {
        LimitRemaining remainingRolls = this.getRemainingRolls(player, reward);
        if (remainingRolls.isExhausted()) {
            RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
            return limit != null && limit.isAlternativeEnabled();
        }

        return true;
    }

    public void incrementRolls(Player player, Reward reward) {
        RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        if (limit == null || !limit.isEnabled()) return;

        UUID playerId = player.getUniqueId();
        Identifier rewardId = reward.getId();

        LimitOptions globalOptions = limit.getGlobalOptions();
        LimitOptions individualOptions = limit.getIndividualOptions();

        if (globalOptions.isEnabled()) {
            this.incrementRolls(GLOBAL_ID, rewardId, 1);
        }
        if (individualOptions.isEnabled()) {
            this.incrementRolls(playerId, rewardId, 1);
        }
    }

    private void incrementRolls(UUID playerId, Identifier rewardId, int amount) {
        RewardLimitData data = this.cachedDataService.getCachedOrCreate(playerId, rewardId, CacheStrategy.PERMANENT);

        data.setRolls(data.getRolls() + amount);

        this.cachedDataService.markDirty(data);
    }

    public LimitRemaining getRemainingRolls(Player player, Reward reward) {
        RewardLimitComponent limit = reward.getComponentOrNull(RewardComponentKeys.LIMIT);
        if (limit == null || !limit.isEnabled()) return DefaultLimitRemaining.unlimited();

        Identifier rewardId = reward.getId();
        UUID playerId = player.getUniqueId();

        LimitOptions globalOptions = limit.getGlobalOptions();
        LimitOptions individualOptions = limit.getIndividualOptions();

        LimitRemaining globalLeft = this.calculateRemaining(globalOptions, () -> this.getGlobalRolls(rewardId));
        LimitRemaining individualLeft = this.calculateRemaining(individualOptions, () -> this.getIndividualRolls(
            playerId,
            rewardId));

        return this.getStrictestLimit(globalLeft, individualLeft);
    }

    private LimitRemaining calculateRemaining(LimitOptions options, IntSupplier rollsSupplier) {
        if (!options.isEnabled()) {
            return DefaultLimitRemaining.unlimited();
        }

        int remaining = options.getAmount() - rollsSupplier.getAsInt();

        return DefaultLimitRemaining.limited(remaining);
    }

    private LimitRemaining getStrictestLimit(LimitRemaining... remainingRolls) {
        return Stream.of(remainingRolls)
            .filter(remaining -> !remaining.isUnlimited())
            .min(Comparator.comparingInt(LimitRemaining::getRemaining))
            .orElse(DefaultLimitRemaining.unlimited());
    }

    public int getGlobalRolls(Identifier rewardId) {
        return this.getRolls(GLOBAL_ID, rewardId);
    }

    public int getIndividualRolls(UUID playerId, Identifier rewardId) {
        return this.getRolls(playerId, rewardId);
    }

    private int getRolls(UUID playerId, Identifier rewardId) {
        RewardLimitData data = this.cachedDataService.getCached(playerId, rewardId).orElse(null);
        if (data == null) return 0;

        return data.getRolls();
    }
}
