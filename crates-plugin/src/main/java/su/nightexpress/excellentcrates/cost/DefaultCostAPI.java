package su.nightexpress.excellentcrates.cost;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.CostAPI;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.cost.core.CostService;

@NullMarked
public class DefaultCostAPI implements CostAPI {

    private final IdentifiableRegistry<CostType<?>> types;
    private final CostService                       costService;

    public DefaultCostAPI(IdentifiableRegistry<CostType<?>> types, CostService costService) {
        this.types = types;
        this.costService = costService;
    }

    @Override
    public void registerType(CostType<?> type) {
        this.types.register(type);
    }

    @Override
    public @Nullable CostType<?> getType(Identifier id) {
        return this.types.get(id);
    }

    @Override
    public List<CostType<?>> getAvailableTypes(Crate crate) {
        return costService.getAvailableTypes(crate);
    }

    @Override
    public <T extends CostOption> List<T> getAvailableOptions(Crate crate, CostType<T> type) {
        return costService.getAvailableOptions(crate, type);
    }

    @Override
    public boolean hasMultipleCosts(Crate crate) {
        return costService.hasMultipleCosts(crate);
    }

    @Override
    public <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type,
                                                               String optionId,
                                                               int amount) {
        return costService.checkAffordance(player, crate, type, optionId, amount);
    }

    @Override
    public <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type, T option,
                                                               int amount) {
        return costService.checkAffordance(player, crate, type, option, amount);
    }
}
