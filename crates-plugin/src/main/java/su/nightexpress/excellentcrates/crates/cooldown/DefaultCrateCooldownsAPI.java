package su.nightexpress.excellentcrates.crates.cooldown;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownsAPI;

@NullMarked
public class DefaultCrateCooldownsAPI implements CrateCooldownsAPI {

    private final CrateCooldownService cooldownService;

    public DefaultCrateCooldownsAPI(CrateCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    @Override
    public CompletableFuture<List<CrateCooldownData>> loadGlobalCooldowns() {
        return cooldownService.loadGlobalCooldowns();
    }

    @Override
    public boolean isOnCooldown(Player player, Crate crate) {
        return cooldownService.isOnCooldown(player, crate);
    }

    @Override
    public boolean isOnTemporalCooldown(Player player, Crate crate) {
        return cooldownService.isOnTemporalCooldown(player, crate);
    }

    @Override
    public boolean isOnPermanentCooldown(Player player, Crate crate) {
        return cooldownService.isOnPermanentCooldown(player, crate);
    }

    @Override
    public @Nullable CooldownTimestamp getExpirationTimestamp(Player player, Crate crate) {
        return cooldownService.getExpirationTimestamp(player, crate);
    }

    @Override
    public void applyCooldowns(Player player, Crate crate) {
        cooldownService.applyCooldowns(player, crate);
    }
}
