package su.nightexpress.excellentcrates.api.crate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.manager.ICleanable;
import su.nexmedia.engine.api.manager.IPlaceholder;
import su.nexmedia.engine.api.menu.IMenu;

public interface ICrateMenu extends ICleanable, IPlaceholder {

    String PLACEHOLDER_ID = "%menu_id%";

    @NotNull String getId();

    @NotNull IMenu getMenu();

    void open(@NotNull Player player);
}
