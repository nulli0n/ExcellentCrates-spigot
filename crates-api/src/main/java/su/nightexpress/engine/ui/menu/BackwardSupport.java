package su.nightexpress.engine.ui.menu;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface BackwardSupport {

    BackwardNavigator backwardNavigator();

    default void moveBackward(Player player) {
        BackwardNavigator navigator = this.backwardNavigator();
        if (navigator != null) {
            navigator.moveBack(player);
        }
    }
}
