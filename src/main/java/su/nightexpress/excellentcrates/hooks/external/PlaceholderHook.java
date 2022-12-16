package su.nightexpress.excellentcrates.hooks.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.data.CrateUser;

public class PlaceholderHook {

    private static CratesExpansion expansion;

    public static void setup() {
        expansion = new CratesExpansion();
        expansion.register();
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
        }
    }

    static class CratesExpansion extends PlaceholderExpansion {

        @Override
        @NotNull
        public String getIdentifier() {
            return ExcellentCratesAPI.PLUGIN.getName().toLowerCase();
        }

        @Override
        @NotNull
        public String getAuthor() {
            return ExcellentCratesAPI.PLUGIN.getAuthor();
        }

        @Override
        @NotNull
        public String getVersion() {
            return ExcellentCratesAPI.PLUGIN.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public String onPlaceholderRequest(@Nullable Player player, @NotNull String tmp) {
            if (player == null) return null;

            ExcellentCrates plugin = ExcellentCratesAPI.PLUGIN;
            if (tmp.startsWith("keys_")) {
                String id = tmp.replace("keys_", "");
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate != null) {
                    int keys = plugin.getKeyManager().getKeysAmount(player, crate);
                    return String.valueOf(keys);
                }
            }
            else if (tmp.startsWith("cooldown_")) {
                String id = tmp.replace("cooldown_", "");
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate != null) {
                    CrateUser user = plugin.getUserManager().getUserData(player);

                    long left = user.getCrateCooldown(crate);
                    if (left == 0) return plugin.getMessage(Lang.CRATE_PLACEHOLDER_COOLDOWN_BLANK).getLocalized();
                    return TimeUtil.formatTimeLeft(left);
                }
            }

            return null;
        }
    }
}
