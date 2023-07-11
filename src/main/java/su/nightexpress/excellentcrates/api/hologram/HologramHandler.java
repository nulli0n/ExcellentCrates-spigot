package su.nightexpress.excellentcrates.api.hologram;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.Loadable;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateReward;

public interface HologramHandler extends Loadable {

    void create(@NotNull Crate crate);

    void remove(@NotNull Crate crate);

    default void update(@NotNull Crate crate) {
        this.remove(crate);
        this.create(crate);
    }

    void createReward(@NotNull Player player, @NotNull CrateReward reward, @NotNull Location location);

    void removeReward(@NotNull Player player);
}
