package su.nightexpress.excellentcrates.api.cost;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.excellentcrates.api.cost.type.CostTypeOption;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CostSelectionHandler {

    void startSelection(Player player, Crate crate, ProcessCallback<CostTypeOption> callback);

    void selectAnyAvailableCost(Player player, Crate crate, ProcessCallback<CostTypeOption> callback);
}
