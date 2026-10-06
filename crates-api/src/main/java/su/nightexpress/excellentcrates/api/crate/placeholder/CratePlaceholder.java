package su.nightexpress.excellentcrates.api.crate.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CratePlaceholder {

    PlaceholderApplier apply(Crate crate);

    PlaceholderApplier apply(Crate crate, Player player);
}
