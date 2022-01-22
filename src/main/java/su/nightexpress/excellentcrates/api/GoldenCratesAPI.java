package su.nightexpress.excellentcrates.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.crate.CrateManager;
import su.nightexpress.excellentcrates.data.CrateUser;
import su.nightexpress.excellentcrates.data.UserManager;
import su.nightexpress.excellentcrates.key.KeyManager;
import su.nightexpress.excellentcrates.menu.MenuManager;

@Deprecated
public class GoldenCratesAPI {

    private static ExcellentCrates plugin = ExcellentCrates.getInstance();

    @Nullable
    public static CrateUser getUserData(@NotNull Player player) {
        return plugin.getUserManager().getOrLoadUser(player);
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

    @NotNull
    public static MenuManager getMenuManager() {
        return plugin.getMenuManager();
    }
}
