package su.nightexpress.excellentcrates.api.crate.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CratePlaceholders {

    TinyRegistry<CratePlaceholder> getPlaceholders();

    void registerPlaceholder(CratePlaceholder placeholder);

    PlaceholderApplier allPlaceholders(Crate crate);

    PlaceholderApplier allPlaceholders(Crate crate, @Nullable Player player);

    PlaceholderApplier playerPlaceholders(Crate crate, Player player);

    PlaceholderApplier basePlaceholders(Crate crate);
}
