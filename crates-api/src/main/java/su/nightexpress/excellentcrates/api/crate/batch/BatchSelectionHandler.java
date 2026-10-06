package su.nightexpress.excellentcrates.api.crate.batch;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface BatchSelectionHandler {

    void startSelection(Player player, Crate crate, int maxAllowed, ProcessCallback<Integer> callback);
}
