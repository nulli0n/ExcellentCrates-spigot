package su.nightexpress.excellentcrates.api.key.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.key.CrateKey;

@NullMarked
public interface KeyPlaceholders {

    void registerPlaceholder(KeyPlaceholder placeholder);

    TinyRegistry<KeyPlaceholder> getPlaceholders();

    PlaceholderApplier allPlaceholders(CrateKey key);

    /**
     * Returns a consumer that applies all placeholders for the given key and player.
     */
    PlaceholderApplier allPlaceholders(CrateKey key, @Nullable Player player);

    PlaceholderApplier basePlaceholders(CrateKey key);

    PlaceholderApplier basePlaceholders(CrateKey key, @Nullable Player player);
}
