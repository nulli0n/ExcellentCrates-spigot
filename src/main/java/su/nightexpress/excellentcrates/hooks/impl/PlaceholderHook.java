package su.nightexpress.excellentcrates.hooks.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.TimeUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PlaceholderHook {

    private static Expansion expansion;

    public static void setup(@NotNull CratesPlugin plugin) {
        if (expansion == null) {
            expansion = new Expansion(plugin);
            expansion.register();
        }
    }

    public static void shutdown() {
        if (expansion != null) {
            expansion.unregister();
            expansion = null;
        }
    }

    static class Expansion extends PlaceholderExpansion {

        private final CratesPlugin                                   plugin;
        private final Map<String, BiFunction<Player, Crate, String>> placeholders;

        public Expansion(@NotNull CratesPlugin plugin) {
            this.plugin = plugin;
            this.placeholders = new LinkedHashMap<>();

            this.placeholders.put("keys", (player, crate) -> {
                int keys = plugin.getKeyManager().getKeysAmount(player, crate);
                return NumberUtil.format(keys);
            });

            this.placeholders.put("openings", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getOrFetch(player);
                return NumberUtil.format(user.getCrateData(crate).getOpenings());
            });

            this.placeholders.put("cooldown", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getOrFetch(player);
                UserCrateData data = user.getCrateData(crate);
                if (!data.hasCooldown()) return Config.CRATE_COOLDOWN_FORMATTER_READY.get();

                LocalDateTime time = TimeUtil.getLocalDateTimeOf(data.getOpenCooldown());
                LocalDateTime now = LocalDateTime.now();
                Duration duration = Duration.between(now, time);

                return Config.CRATE_COOLDOWN_FORMATTER_TIME.get()
                    .replace("hh", String.valueOf(duration.toHours()))
                    .replace("mm", String.valueOf(duration.toMinutesPart()))
                    .replace("ss", String.valueOf(duration.toSecondsPart()));
            });

            this.placeholders.put("next_milestone_openings", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getOrFetch(player);
                int milestones = user.getCrateData(crate).getMilestone();
                Milestone milestone = crate.getNextMilestone(milestones);
                return milestone == null ? "-" : NumberUtil.format(milestone.getOpenings() - milestones);
            });

            this.placeholders.put("next_milestone_reward", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getOrFetch(player);
                int milestones = user.getCrateData(crate).getMilestone();
                Milestone milestone = crate.getNextMilestone(milestones);
                Reward reward = milestone == null ? null : milestone.getReward();

                return reward == null ? "-" : reward.getName();
            });

            this.placeholders.put("latest_opener", (player, crate) -> {
                String latest = crate.getLatestOpener();
                return latest == null ? "-" : latest;
            });

            this.placeholders.put("latest_rolled_reward", (player, crate) -> {
                String latest = crate.getLatestReward();
                return latest == null ? "-" : latest;
            });
        }

        @Override
        @NotNull
        public String getIdentifier() {
            return this.plugin.getName().toLowerCase();
        }

        @Override
        @NotNull
        public String getAuthor() {
            return this.plugin.getDescription().getAuthors().getFirst();
        }

        @Override
        @NotNull
        public String getVersion() {
            return this.plugin.getDescription().getVersion();
        }

        @Override
        public boolean persist() {
            return true;
        }

        @Override
        public String onPlaceholderRequest(@Nullable Player player, @NotNull String params) {
            if (player == null) return null;

            for (var entry : this.placeholders.entrySet()) {
                String prefix = entry.getKey() + "_";
                if (!params.startsWith(prefix)) continue;

                String id = params.substring(prefix.length());
                Crate crate = plugin.getCrateManager().getCrateById(id);
                if (crate == null) return null;

                return entry.getValue().apply(player, crate);
            }

            return null;
        }
    }
}
