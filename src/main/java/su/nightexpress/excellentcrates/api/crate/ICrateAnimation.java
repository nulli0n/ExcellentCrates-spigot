package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ConfigHolder;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.menu.IMenu;

public interface ICrateAnimation extends ConfigHolder, ICleanable {

    @NotNull String getId();

    @NotNull IMenu getMenu();

    void open(@NotNull Player player, @NotNull ICrate crate);

    boolean isOpening(@NotNull Player player);
}
