package su.nightexpress.excellentcrates.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.hologram.entity.FakeEntity;

import java.util.Set;

public interface HologramHandler {

    void sendHologramPackets(@NotNull Player player, @NotNull FakeEntity entity, boolean needSpawn, @NotNull String textLine);

    void sendDestroyEntityPacket(@NotNull Player player, @NotNull Set<Integer> idList);

    void sendDestroyEntityPacket(@NotNull Set<Integer> idList);
}
