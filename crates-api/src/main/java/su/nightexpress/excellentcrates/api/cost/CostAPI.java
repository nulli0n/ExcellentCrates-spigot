package su.nightexpress.excellentcrates.api.cost;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CostAPI extends PluginAPI {

    void registerType(CostType<?> type);

    @Nullable
    CostType<?> getType(Identifier id);

    List<CostType<?>> getAvailableTypes(Crate crate);

    <T extends CostOption> List<T> getAvailableOptions(Crate crate, CostType<T> type);

    boolean hasMultipleCosts(Crate crate);

    <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type,
                                                        String optionId,
                                                        int amount);

    <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type, T option,
                                                        int amount);
}
