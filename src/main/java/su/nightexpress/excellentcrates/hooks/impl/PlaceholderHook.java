package su.nightexpress.excellentcrates.hooks.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.utils.NumberUtil;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.impl.CrateUser;

import java.time.Duration;
import java.time.LocalDateTime;

public class PlaceholderHook {

    private static CratesExpansion expansion;

    public static void setup() {
        if (expansion == null) {
            expansion = new CratesExpansion();
            expansion.register();
        }
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
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
            return ExcellentCratesAPI.PLUGIN.getDescription().getAuthors().get(0);
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
        public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
            if (player == null) return null;

            ExcellentCrates plugin = ExcellentCratesAPI.PLUGIN;
            if (params.startsWith("keys_")) {
                String id = params.substring("keys_".length());
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) return null;

                int keys = plugin.getKeyManager().getKeysAmount(player, crate);
                return NumberUtil.format(keys);
            }
            if (params.startsWith("openings_")) {
                String id = params.substring("openings_".length());
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) return null;

                CrateUser user = plugin.getUserManager().getUserData(player);
                return NumberUtil.format(user.getOpeningsAmount(crate));
            }
            else if (params.startsWith("cooldown_")) {
                String id = params.substring("cooldown_".length());
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) return null;

                CrateUser user = plugin.getUserManager().getUserData(player);

                long left = user.getCrateCooldown(crate);
                if (left == 0) return Config.CRATE_COOLDOWN_FORMATTER_READY.get();

                LocalDateTime time = TimeUtil.getLocalDateTimeOf(left);
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, time);

                return Config.CRATE_COOLDOWN_FORMATTER_TIME.get()
                    .replace("hh", String.valueOf(duration.toHours()))
                    .replace("mm", String.valueOf(duration.toMinutesPart()))
                    .replace("ss", String.valueOf(duration.toSecondsPart()))
                    ;
            }

            return null;
        }
    }
}
