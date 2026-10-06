package su.nightexpress.excellentcrates.api.crate.cooldown;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateCooldownsAPI {

    CompletableFuture<List<CrateCooldownData>> loadGlobalCooldowns();

    boolean isOnCooldown(Player player, Crate crate);

    boolean isOnTemporalCooldown(Player player, Crate crate);

    boolean isOnPermanentCooldown(Player player, Crate crate);

    @Nullable
    CooldownTimestamp getExpirationTimestamp(Player player, Crate crate);

    void applyCooldowns(Player player, Crate crate);
}
