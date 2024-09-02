package su.nightexpress.excellentcrates.hooks.impl;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Milestone;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
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
        private final Map<String, BiFunction<Player, Crate, String>> cratePlaceholders;

        public Expansion(@NotNull CratesPlugin plugin) {
            this.plugin = plugin;
            this.cratePlaceholders = new LinkedHashMap<>();

            this.cratePlaceholders.put("keys", (player, crate) -> {
                int keys = plugin.getKeyManager().getKeysAmount(player, crate);
                return NumberUtil.format(keys);
            });

            this.cratePlaceholders.put("openings", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getUserData(player);
                return NumberUtil.format(user.getOpeningsAmount(crate));
            });

            this.cratePlaceholders.put("cooldown", (player, crate) -> {
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
            });

            this.cratePlaceholders.put("next_milestone_openings", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getUserData(player);
                int milestones = user.getMilestones(crate);
                Milestone milestone = crate.getNextMilestone(milestones);
                return milestone == null ? "-" : NumberUtil.format(milestone.getOpenings() - milestones);
            });

            this.cratePlaceholders.put("next_milestone_reward", (player, crate) -> {
                CrateUser user = plugin.getUserManager().getUserData(player);
                int milestones = user.getMilestones(crate);
                Milestone milestone = crate.getNextMilestone(milestones);
                Reward reward = milestone == null ? null : milestone.getReward();

                return reward == null ? "-" : reward.getName();
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
            return this.plugin.getDescription().getAuthors().get(0);
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

            for (var entry : this.cratePlaceholders.entrySet()) {
                String prefix = entry.getKey() + "_";
                if (!params.startsWith(prefix)) continue;

                var function = entry.getValue();

                return this.parse(params, prefix, player, function);
            }

            return null;
        }

        @Nullable
        private String parse(@NotNull String params, @NotNull String prefix, @NotNull Player player, @NotNull BiFunction<Player, Crate, String> function) {
            String id = params.substring(prefix.length());
            Crate crate = plugin.getCrateManager().getCrateById(id);
            if (crate == null) return null;

            return function.apply(player, crate);
        }
    }
}
