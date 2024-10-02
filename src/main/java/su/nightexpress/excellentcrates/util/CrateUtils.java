package su.nightexpress.excellentcrates.util;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.*;

public class CrateUtils {

    private static final Map<Player, Crate> ASSIGN_BLOCK_MAP = new WeakHashMap<>();

    public static void setAssignBlockCrate(@NotNull Player player, @NotNull Crate crate) {
        ASSIGN_BLOCK_MAP.put(player, crate);
    }

    @Nullable
    public static Crate getAssignBlockCrate(@NotNull Player player) {
        return ASSIGN_BLOCK_MAP.remove(player);
    }

    public static long createTimestamp(long seconds) {
        return seconds < 0 ? -1L : System.currentTimeMillis() + seconds * 1000L;
    }

    @NotNull
    public static Set<Player> getPlayersForEffects(@NotNull Location location) {
        Set<Player> players = new HashSet<>(Bukkit.getServer().getOnlinePlayers());
        players.removeIf(player -> !isInEffectRange(player, location));

        return players;
    }

    public static boolean isInEffectRange(@NotNull Player player, @NotNull Location location) {
        World world = location.getWorld();
        int distance = Config.CRATE_EFFECTS_VISIBILITY_DISTANCE.get();

        return player.getWorld() == world && player.getLocation().distance(location) <= distance;
    }

    @NotNull
    public static String validateId(@NotNull String id) {
        return validateId(id, false);
    }

    @NotNull
    public static String validateId(@NotNull String id, boolean remap) {
        id = StringUtil.lowerCaseUnderscoreStrict(id);
        if (id.replace("_", "").isBlank()) {
            id = remap ? UUID.randomUUID().toString() : "";
        }
        return id;
    }

    public static boolean isSupportedParticle(@NotNull Particle particle) {
        return particle != Particle.VIBRATION && particle != Particle.DUST_COLOR_TRANSITION;
    }

    public static boolean isSupportedParticleData(@NotNull UniParticle particle) {
        return particle.getParticle() != null && isSupportedParticleData(particle.getParticle().getDataType());
    }

    public static boolean isSupportedParticleData(@NotNull Class<?> clazz) {
        return clazz != Void.class && clazz != Vibration.class && clazz != Particle.DustTransition.class;
    }
}
