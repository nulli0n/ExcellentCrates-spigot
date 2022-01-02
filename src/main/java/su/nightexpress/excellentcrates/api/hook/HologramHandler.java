package su.nightexpress.excellentcrates.api.hook;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;

public interface HologramHandler {

    void create(@NotNull ICrate crate);

    void remove(@NotNull ICrate crate);

    default void update(@NotNull ICrate crate) {
        this.remove(crate);
        this.create(crate);
    }

    void createReward(@NotNull Player player, @NotNull ICrateReward reward, @NotNull Location location);

    void removeReward(@NotNull Player player);
}
