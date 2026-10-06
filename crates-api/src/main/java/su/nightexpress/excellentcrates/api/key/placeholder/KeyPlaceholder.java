package su.nightexpress.excellentcrates.api.key.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public interface KeyPlaceholder {

    Consumer<PlaceholderContext.Builder> applyBase(CrateKey key, @Nullable Player player);
}
