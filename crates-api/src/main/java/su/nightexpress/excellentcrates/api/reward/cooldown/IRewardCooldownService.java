package su.nightexpress.excellentcrates.api.reward.cooldown;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface IRewardCooldownService {

    CompletableFuture<List<RewardCooldownData>> loadGlobalCooldowns();

    boolean isOnCooldown(Player player, Reward reward);

    boolean isOnTemporalCooldown(Player player, Reward reward);

    boolean isOnPermanentCooldown(Player player, Reward reward);

    @Nullable
    CooldownTimestamp getExpirationTimestamp(Player player, Reward reward);

    void applyCooldowns(Player player, Reward reward);
}
