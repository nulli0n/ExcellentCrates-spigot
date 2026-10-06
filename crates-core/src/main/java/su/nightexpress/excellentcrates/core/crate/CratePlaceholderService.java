package su.nightexpress.excellentcrates.core.crate;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholder;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;

@NullMarked
public class CratePlaceholderService implements CratePlaceholders {

    private final TinyRegistry<CratePlaceholder> placeholders;

    public CratePlaceholderService() {
        this.placeholders = new SimpleRegistry<>();
    }

    @Override
    public TinyRegistry<CratePlaceholder> getPlaceholders() {
        return this.placeholders;
    }

    @Override
    public void registerPlaceholder(CratePlaceholder placeholder) {
        this.placeholders.register(placeholder);
    }

    @Override
    public PlaceholderApplier allPlaceholders(Crate crate) {
        return this.allPlaceholders(crate, null);
    }

    @Override
    public PlaceholderApplier allPlaceholders(Crate crate, @Nullable Player player) {
        return builder -> {
            if (player != null) {
                builder.apply(this.playerPlaceholders(crate, player));
            }
            builder.apply(this.basePlaceholders(crate));
        };
    }

    @Override
    public PlaceholderApplier playerPlaceholders(Crate crate, Player player) {
        return builder -> {
            this.placeholders.forEach(placeholder -> {
                builder.apply(placeholder.apply(crate, player));
            });
        };
    }

    @Override
    public PlaceholderApplier basePlaceholders(Crate crate) {
        return builder -> {
            builder.with(SharedPlaceholders.CRATE_ID, () -> crate.id().value());
            builder.with(SharedPlaceholders.CRATE_NAME, () -> crate.getDisplay().getName());
            builder.with(SharedPlaceholders.CRATE_DESCRIPTION, () -> String.join("\n", crate.getDisplay().getLore()));
        };
    }
}
