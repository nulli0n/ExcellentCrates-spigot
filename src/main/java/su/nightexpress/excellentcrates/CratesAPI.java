package su.nightexpress.excellentcrates;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.addon.CratesAddon;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.excellentcrates.user.UserManager;

public class CratesAPI {

    private static CratesPlugin plugin;

    static void load(@NotNull CratesPlugin instance) {
        plugin = instance;
    }

    static void clear() {
        plugin = null;
    }
    
    public static boolean isLoaded() {
        return plugin != null;
    }

    @NotNull
    @Deprecated
    public static CratesPlugin getPlugin() {
        return plugin();
    }

    @NotNull
    public static CratesPlugin plugin() {
        if (plugin == null) throw new IllegalStateException("The API is not yet loaded!");
        
        return plugin;
    }

    public static void registerAddon(@NotNull CratesAddon addon) {
        plugin().registerAddon(addon);
    }

    @NotNull
    public static CrateUser getUserData(@NotNull Player player) {
        return plugin.getUserManager().getOrFetch(player);
    }

    @NotNull
    public static UserManager getUserManager() {
        return plugin.getUserManager();
    }

    @NotNull
    public static CrateManager getCrateManager() {
        return plugin.getCrateManager();
    }

    @NotNull
    public static KeyManager getKeyManager() {
        return plugin.getKeyManager();
    }

//    @NotNull
//    public static MenuManager getMenuManager() {
//        return plugin.getMenuManager();
//    }

    public static void info(@NotNull String msg) {
        plugin.info(msg);
    }

    public static void warn(@NotNull String msg) {
        plugin.warn(msg);
    }

    public static void error(@NotNull String msg) {
        plugin.error(msg);
    }
}
