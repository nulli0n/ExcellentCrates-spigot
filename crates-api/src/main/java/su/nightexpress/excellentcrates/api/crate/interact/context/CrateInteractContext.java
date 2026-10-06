package su.nightexpress.excellentcrates.api.crate.interact.context;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateInteractContext {

    Player player();

    Crate crate();
}
