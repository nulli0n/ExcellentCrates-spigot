package su.nightexpress.excellentcrates.api.crate.hologram;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface HologramTextProvider {

    List<String> getText(Player player);
}
