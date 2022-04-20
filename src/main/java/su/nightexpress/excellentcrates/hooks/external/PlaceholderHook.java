package su.nightexpress.excellentcrates.hooks.external;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.hook.AbstractHook;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.data.CrateUser;

public class PlaceholderHook extends AbstractHook<ExcellentCrates> {

    private CratesExpansion expansion;

    public PlaceholderHook(@NotNull ExcellentCrates plugin, @NotNull String pluginName) {
        super(plugin, pluginName);
    }

    @Override
    public boolean setup() {
        this.expansion = new CratesExpansion();
        this.expansion.register();

        return true;
    }

    @Override
    public void shutdown() {
        if (this.expansion != null) {
            this.expansion.unregister();
        }
    }

    class CratesExpansion extends PlaceholderExpansion {

        @Override
        @NotNull
        public String getIdentifier() {
            return plugin.getName().toLowerCase();
        }

        @Override
        @NotNull
        public String getAuthor() {
            return plugin.getAuthor();
        }

        @Override
        @NotNull
        public String getVersion() {
            return plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public String onPlaceholderRequest(@Nullable Player player, @NotNull String tmp) {
            if (player == null) return null;

            if (tmp.startsWith("keys_")) {
                String id = tmp.replace("keys_", "");
                ICrate crate = plugin.getCrateManager().getCrateById(id);
                if (crate != null) {
                    int keys = plugin.getKeyManager().getKeysAmount(player, crate);
                    return String.valueOf(keys);
                }
            }
            else if (tmp.startsWith("cooldown_")) {
                String id = tmp.replace("cooldown_", "");
                ICrate crate = plugin.getCrateManager().getCrateById(id);
                if (crate != null) {
                    CrateUser user = plugin.getUserManager().getOrLoadUser(player);

                    long left = user.getCrateCooldown(crate);
                    if (left == 0) return plugin.lang().Crate_Placeholder_Cooldown_Blank.getLocalized();
                    return TimeUtil.formatTimeLeft(left);
                }
            }

            return null;
        }
    }
}
