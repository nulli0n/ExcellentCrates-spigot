package su.nightexpress.excellentcrates.keys.storage.controller;

import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletionException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.cache.CacheStrategy;
import su.nightexpress.engine.component.BaseController;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.keys.storage.db.KeyStorageCachedDataService;
import su.nightexpress.nightcore.util.Players;

@NullMarked
public class KeyStoragePlayerSessionController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoragePlayerSessionController.class);

    private final KeyStorageCachedDataService cacheService;

    public KeyStoragePlayerSessionController(CratesPlugin plugin, KeyStorageCachedDataService cacheService) {
        super(plugin);
        this.cacheService = cacheService;
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

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            return;
        }

        UUID playerId = event.getUniqueId();

        try {
            this.cacheService.loadAndCacheAsync(playerId, CacheStrategy.TEMPORARY).join();
        }
        catch (CancellationException | CompletionException exception) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);

            Players.disallowLogin(event, Result.KICK_OTHER,
                "An error occurred while loading your keys data. Please try again later.");

            LOGGER.error("Failed to load keys data for player {}", playerId);
            LOGGER.error("Reason: ", exception);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        this.cacheService.updateCacheStrategy(playerId, CacheStrategy.PERMANENT);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();

        this.cacheService.updateCacheStrategy(playerId, CacheStrategy.TEMPORARY);
    }
}
