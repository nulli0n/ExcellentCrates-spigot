package su.nightexpress.engine.ui.menu;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface BackwardNavigator {

    static BackwardNavigator CLOSE_INVENTORY = Player::closeInventory;

    void moveBack(Player player);
}
