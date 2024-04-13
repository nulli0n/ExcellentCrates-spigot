package su.nightexpress.excellentcrates.util;

import org.bukkit.Particle;
import org.bukkit.Vibration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.util.wrapper.UniParticle;

import java.util.Map;
import java.util.WeakHashMap;

public class CrateUtils {

    private static final Map<Player, Crate> ASSIGN_BLOCK_MAP = new WeakHashMap<>();

    public static void setAssignBlockCrate(@NotNull Player player, @NotNull Crate crate) {
        ASSIGN_BLOCK_MAP.put(player, crate);
    }

    @Nullable
    public static Crate getAssignBlockCrate(@NotNull Player player) {
        return ASSIGN_BLOCK_MAP.remove(player);
    }

    public static boolean isSupportedParticle(@NotNull Particle particle) {
        return particle != Particle.VIBRATION && particle != Particle.DUST_COLOR_TRANSITION;
    }

    public static boolean isSupportedParticleData(@NotNull UniParticle particle) {
        return isSupportedParticleData(particle.getParticle().getDataType());
    }

    public static boolean isSupportedParticleData(@NotNull Class<?> clazz) {
        return clazz != Void.class && clazz != Vibration.class && clazz != Particle.DustTransition.class;
    }
}
