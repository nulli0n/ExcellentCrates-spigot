package su.nightexpress.excellentcrates.crates.cooldown;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownTimestamp;
import su.nightexpress.excellentcrates.api.common.cooldown.CooldownType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.crate.cooldown.CrateCooldownData;
import su.nightexpress.excellentcrates.api.crate.cooldown.component.CrateCooldownComponent;
import su.nightexpress.excellentcrates.crates.cooldown.db.CrateCooldownCachedDataService;

@NullMarked
public class CrateCooldownService {

    private static final UUID GLOBAL_ID = new UUID(0, 0);

    private final CrateCooldownCachedDataService dataService;

    public CrateCooldownService(CrateCooldownCachedDataService dataService) {
        this.dataService = dataService;
    }

    public CompletableFuture<List<CrateCooldownData>> loadGlobalCooldowns() {
        return this.dataService.loadAndCacheAsync(GLOBAL_ID, CacheStrategy.PERMANENT);
    }

    public boolean isOnCooldown(Player player, Crate crate) {
        return this.isOnTemporalCooldown(player, crate) || this.isOnPermanentCooldown(player, crate);
    }

    public boolean isOnTemporalCooldown(Player player, Crate crate) {
        CooldownTimestamp timestamp = this.getExpirationTimestamp(player, crate);
        return timestamp != null && !timestamp.isPermanent() && !timestamp.isExpired();
    }

    public boolean isOnPermanentCooldown(Player player, Crate crate) {
        CooldownTimestamp timestamp = this.getExpirationTimestamp(player, crate);
        return timestamp != null && timestamp.isPermanent();
    }

    public @Nullable CooldownTimestamp getExpirationTimestamp(Player player, Crate crate) {
        CooldownTimestamp globalTimestamp = this.getCachedTimestamp(crate, GLOBAL_ID);
        CooldownTimestamp playerTimestamp = this.getCachedTimestamp(crate, player.getUniqueId());

        return this.getStrictestCooldown(globalTimestamp, playerTimestamp);
    }

    private @Nullable CooldownTimestamp getCachedTimestamp(Crate crate, UUID targetId) {
        CrateCooldownData data = this.dataService.getCached(targetId, crate.id()).orElse(null);
        if (data == null || data.isExpired()) return null;

        if (data.isPermanent()) return CooldownTimestamp.permanent();

        return CooldownTimestamp.temporal(data.getExpirationTimestamp());
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

    public @Nullable CrateCooldownComponent getCrateCooldowns(Crate crate) {
        return crate.getComponentOrNull(CrateComponentKeys.COOLDOWN);
    }

    public boolean hasCooldownConfigured(Crate crate) {
        CrateCooldownComponent cooldowns = this.getCrateCooldowns(crate);
        if (cooldowns == null) return false;

        for (CooldownType type : CooldownType.values()) {
            if (cooldowns.getCooldown(type).isEffectivelyEnabled()) {
                return true;
            }
        }

        return false;
    }

    public void applyCooldowns(Player player, Crate crate) {
        CrateCooldownComponent cooldowns = this.getCrateCooldowns(crate);
        if (cooldowns == null) return;

        CooldownOptions globalCooldown = cooldowns.getGlobalCooldown();
        CooldownOptions individualCooldown = cooldowns.getIndividualCooldown();

        Identifier crateId = crate.id();
        UUID playerId = player.getUniqueId();

        if (globalCooldown.isEffectivelyEnabled()) {
            CrateCooldownData data = this.dataService.getCachedOrCreate(GLOBAL_ID, crateId, CacheStrategy.PERMANENT);
            data.setPermanent(false);
            data.setExpirationTimestamp(globalCooldown.createCooldownTimestamp());
            this.dataService.markDirty(data);
        }

        if (individualCooldown.isEffectivelyEnabled()) {
            CrateCooldownData data = this.dataService.getCachedOrCreate(playerId, crateId, CacheStrategy.PERMANENT);
            data.setPermanent(false);
            data.setExpirationTimestamp(individualCooldown.createCooldownTimestamp());
            this.dataService.markDirty(data);
        }
    }
}
