package su.nightexpress.excellentcrates.cost.core;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.cost.type.CostDisplayProvider;
import su.nightexpress.excellentcrates.api.cost.type.CostLogicProvider;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.cost.lang.CostLang;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CostService {

    private static final int MIN_COSTS_FOR_GUI = 0;

    private final IdentifiableRegistry<CostType<?>> types;

    public CostService(IdentifiableRegistry<CostType<?>> types) {
        this.types = types;
    }

    /**
     * Get all cost evaluators that have a cost for the given crate.
     *
     * @param crate the crate to get evaluators for
     * @return a list of cost evaluators that have a cost for the crate
     */
    public List<CostType<?>> getAvailableTypes(Crate crate) {
        return this.types.values().stream()
            .filter(type -> type.getLogic().hasCost(crate))
            .toList();
    }

    public <T extends CostOption> List<T> getAvailableOptions(Crate crate, CostType<T> type) {
        return type.getLogic().getOptions(crate);
    }

    public boolean hasMultipleCosts(Crate crate) {
        return this.getAvailableTypes(crate).size() >= MIN_COSTS_FOR_GUI;
    }

    public ActionResult checkAffordance(Player player, Crate crate, Identifier typeId, String optionId, int amount) {
        CostType<?> type = this.types.get(typeId);
        if (type == null) {
            return ActionResult.fail(CostLang.SELECTION_NOT_FOUND);
        }
        return this.checkAffordance(player, crate, type, optionId, amount);
    }

    public <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type,
                                                               String optionId,
                                                               int amount) {
        T option = type.getLogic().getOptionById(crate, optionId);
        if (option == null) {
            return ActionResult.fail(CostLang.SELECTION_NOT_FOUND);
        }

        return this.checkAffordance(player, crate, type, option, amount);
    }

    public <T extends CostOption> ActionResult checkAffordance(Player player, Crate crate, CostType<T> type, T option,
                                                               int amount) {
        CostLogicProvider<T> logic = type.getLogic();
        CostDisplayProvider<T> display = type.getDisplay();

        if (!logic.canAfford(player, crate, option, amount)) {
            return ActionResult.fail(CostLang.SELECTION_NOT_ENOUGH_FUNDS, ctx -> ctx
                .with(CommonPlaceholders.GENERIC_AMOUNT, () -> {
                    return display.getOptionDisplay(crate, option).name();
                })
                .with(SharedPlaceholders.BALANCE, () -> {
                    return display.formatBalance(logic.getBalance(player, crate, option), option);
                })
                .with(SharedPlaceholders.COST, () -> {
                    return display.formatBalance(logic.getCost(crate, option, amount), option);
                })
            );
        }

        return ActionResult.ok();
    }

    public int getMaxAffordableOpens(Player player, Crate crate, Identifier typeId, String optionId) {
        CostType<?> type = this.types.get(typeId);
        if (type == null) {
            return 0;
        }

        return this.getMaxAffordableOpens(player, crate, type, optionId);
    }

    public <T extends CostOption> int getMaxAffordableOpens(Player player, Crate crate, CostType<T> type,
                                                            String optionId) {
        T option = type.getLogic().getOptionById(crate, optionId);
        if (option == null) {
            return 0;
        }

        return this.getMaxAffordableOpens(player, crate, type, option);
    }

    public <T extends CostOption> int getMaxAffordableOpens(Player player, Crate crate, CostType<T> type,
                                                            @NonNull T option) {
        return type.getLogic().getMaxAffordableOpens(player, crate, option);
    }

    public ActionResult takeCost(Player player, Crate crate, Identifier typeId, String optionId, int amount) {
        CostType<?> type = this.types.get(typeId);
        if (type == null) {
            return ActionResult.fail(CostLang.SELECTION_NOT_FOUND);
        }

        return this.takeCost(player, crate, type, optionId, amount);
    }

    public <T extends CostOption> ActionResult takeCost(Player player, Crate crate, CostType<T> type,
                                                        String optionId,
                                                        int amount) {
        T option = type.getLogic().getOptionById(crate, optionId);
        if (option == null) {
            return ActionResult.fail(CostLang.SELECTION_NOT_FOUND);
        }

        return this.takeCost(player, crate, type, option, amount);
    }

    public <T extends CostOption> ActionResult takeCost(Player player, Crate crate, CostType<T> type, T option,
                                                        int amount) {
        ActionResult checkAffordance = this.checkAffordance(player, crate, type, option, amount);
        if (!checkAffordance.success()) {
            return checkAffordance;
        }

        CostLogicProvider<T> logic = type.getLogic();
        CostDisplayProvider<T> display = type.getDisplay();

        logic.take(player, crate, option, amount);

        return ActionResult.ok(CostLang.PIPELINE_WITHDRAWAL_NOTIFY, ctx -> ctx
            .with(CommonPlaceholders.GENERIC_NAME, () -> {
                return display.getOptionDisplay(crate, option).name();
            })
            .with(SharedPlaceholders.BALANCE, () -> {
                return display.formatBalance(logic.getBalance(player, crate, option), option);
            })
            .with(SharedPlaceholders.COST, () -> {
                return display.formatBalance(logic.getCost(crate, option, amount), option);
            })
        );
    }
}
