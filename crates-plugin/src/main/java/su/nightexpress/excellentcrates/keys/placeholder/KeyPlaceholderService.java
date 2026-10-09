package su.nightexpress.excellentcrates.keys.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholder;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;

@NullMarked
public class KeyPlaceholderService implements KeyPlaceholders {

    private final TinyRegistry<KeyPlaceholder> placeholders;

    public KeyPlaceholderService() {
        this.placeholders = new SimpleRegistry<>();
    }

    @Override
    public void registerPlaceholder(KeyPlaceholder placeholder) {
        this.placeholders.register(placeholder);
    }

    @Override
    public TinyRegistry<KeyPlaceholder> getPlaceholders() {
        return this.placeholders;
    }

    @Override
    public PlaceholderApplier allPlaceholders(CrateKey key) {
        return this.allPlaceholders(key, null);
    }

    @Override
    public PlaceholderApplier allPlaceholders(CrateKey key, @Nullable Player player) {
        return this.basePlaceholders(key, player);
    }

    @Override
    public PlaceholderApplier basePlaceholders(CrateKey key) {
        return this.basePlaceholders(key, null);
    }

    @Override
    public PlaceholderApplier basePlaceholders(CrateKey key, @Nullable Player player) {
        return ctx -> {
            ctx.with(SharedPlaceholders.KEY_ID, () -> key.idString());

            ctx.with(SharedPlaceholders.KEY_NAME, () -> {
                return key.getDisplay().getName();
            });

            ctx.with(SharedPlaceholders.KEY_LORE, () -> {
                return String.join("\n", key.getDisplay().getLore());
            });

            this.placeholders.forEach(placeholder -> {
                ctx.apply(placeholder.applyBase(key, player));
            });
        };
    }
}
