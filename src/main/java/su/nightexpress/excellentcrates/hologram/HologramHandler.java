package su.nightexpress.excellentcrates.hologram;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.util.pos.WorldPos;

public interface HologramHandler {

    void create(@NotNull Crate crate);

    void delete(@NotNull Crate crate);

    void update(@NotNull Crate crate);

    void toggle(@NotNull Crate crate, @NotNull WorldPos blockPos, boolean enabled);

    void purge(@NotNull Player player);
}
