package su.nightexpress.excellentcrates.reward.feature.limit.controller;

import java.util.UUID;
import java.util.concurrent.CompletionException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.reward.feature.limit.db.RewardLimitCachedDataService;

@NullMarked
public class RewardLimitsPlayerSessionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardLimitsPlayerSessionController.class);

    private final RewardLimitCachedDataService dataService;

    public RewardLimitsPlayerSessionController(CratesPlugin plugin, RewardLimitCachedDataService dataService) {
        super(plugin);
        this.dataService = dataService;
    }

    @Override
    protected void onControllerReload() {

    }

    @Override
    protected void onControllerShutdown() {

    }

    @Override
    protected void onControllerStart() {

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        UUID playerId = event.getUniqueId();

        try {
            this.dataService.loadAndCacheAsync(playerId, CacheStrategy.TEMPORARY).join();
        }
        catch (CompletionException exception) {
            LOGGER.error("Failed to load and cache reward limit data for player: " + playerId, exception);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        this.dataService.updateCacheStrategy(playerId, CacheStrategy.PERMANENT);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        this.dataService.updateCacheStrategy(playerId, CacheStrategy.TEMPORARY);
    }
}
