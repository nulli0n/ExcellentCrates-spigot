package su.nightexpress.engine.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface PlaceholderAPIResolver {

    @Nullable
    PlaceholderAPIResult handleRequest(@Nullable Player player, String identifier);
}
