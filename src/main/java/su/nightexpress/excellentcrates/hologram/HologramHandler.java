package su.nightexpress.excellentcrates.hologram;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface HologramHandler {

    void displayHolograms(@NotNull Player player, int entityID, boolean create, @NotNull EntityType type, @NotNull Location location, @NotNull String textLine);

    void destroyEntity(@NotNull Player player, @NotNull Set<Integer> idList);

    void destroyEntity(@NotNull Set<Integer> idList);
}
