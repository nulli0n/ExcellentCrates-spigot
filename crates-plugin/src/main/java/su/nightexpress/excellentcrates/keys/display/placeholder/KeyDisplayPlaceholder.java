package su.nightexpress.excellentcrates.keys.display.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.keys.display.KeyDisplayResolver;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class KeyDisplayPlaceholder implements KeyPlaceholder {

    private final KeyDisplayResolver displayResolver;

    public KeyDisplayPlaceholder(KeyDisplayResolver displayResolver) {
        this.displayResolver = displayResolver;
    }

    @Override
    public Consumer<Builder> applyBase(CrateKey key, @Nullable Player player) {
        return ctx -> {
            ctx.with(SharedPlaceholders.KEY_NAME, () -> {
                return this.displayResolver.getDisplayInfo(key).name();
            });

            ctx.with(SharedPlaceholders.KEY_LORE, () -> {
                return String.join("\n", this.displayResolver.getDisplayInfo(key).lore());
            });
        };
    }
}
