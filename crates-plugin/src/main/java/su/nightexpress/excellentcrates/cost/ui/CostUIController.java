package su.nightexpress.excellentcrates.cost.ui;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.ProcessCallback;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.cost.CostSelectionHandler;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.cost.type.CostTypeOption;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateFeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.cost.core.CostService;
import su.nightexpress.excellentcrates.cost.core.DefaultCostTypeOption;
import su.nightexpress.excellentcrates.cost.lang.CostLang;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostCategoriesMenuContext;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostOptionsMenuContext;

@NullMarked
public class CostUIController implements CostSelectionHandler, CrateFeedbackHandler {

    private static final int SINGLE_OPTION_THRESHOLD = 1;

    private final IdentifiableRegistry<CostType<?>> types;
    private final CostService                       costService;
    private final CostUIService                     uiService;
    private final CrateMessageDispatcher            dispatcher;

    public CostUIController(IdentifiableRegistry<CostType<?>> types,
                            CostService costService,
                            CostUIService uiService,
                            CrateMessageDispatcher dispatcher) {
        this.types = types;
        this.costService = costService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public CrateMessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void selectAnyAvailableCost(Player player, Crate crate, ProcessCallback<CostTypeOption> callback) {
        List<CostType<?>> availableTypes = this.costService.getAvailableTypes(crate);

        // No available cost types: ignore
        if (availableTypes.isEmpty()) {
            callback.proceed(null);
            return;
        }

        for (CostType<?> type : availableTypes) {
            CostTypeOption option = this.findFirstAvailableOption(player, crate, type);
            if (option != null) {
                callback.proceed(option);
                return;
            }
        }

        this.dispatcher.sendBase(player, crate, CostLang.SELECTION_NOTHING_AFFORDABLE);

        callback.cancel();
    }

    private <T extends CostOption> @Nullable CostTypeOption findFirstAvailableOption(Player player, Crate crate,
                                                                                     CostType<T> type) {
        List<T> options = this.costService.getAvailableOptions(crate, type);
        for (T option : options) {
            if (type.getLogic().canAfford(player, crate, option, 1)) {
                return new DefaultCostTypeOption(type.id(), option.getIdentifier());
            }
        }

        return null;
    }

    @Override
    public void startSelection(Player player, Crate crate, ProcessCallback<CostTypeOption> callback) {
        List<CostType<?>> availableTypes = this.costService.getAvailableTypes(crate);

        // No available cost types: ignore
        if (availableTypes.isEmpty()) {
            callback.proceed(null);
            return;
        }

        // If there is exactly one available cost type, handle it directly
        if (availableTypes.size() == SINGLE_OPTION_THRESHOLD) {
            CostType<?> singleType = availableTypes.getFirst();
            List<String> options = this.costService.getAvailableOptions(crate, singleType)
                .stream()
                .map(CostOption::getIdentifier)
                .toList();

            // If there is exactly one available option, skip the entire UI and proceed directly
            if (options.size() == SINGLE_OPTION_THRESHOLD) {
                callback.proceed(new DefaultCostTypeOption(singleType.id(), options.getFirst()));
                return;
            }

            // The menu will handle onAbort if the user closes the inventory
            BackwardNavigator close = BackwardNavigator.CLOSE_INVENTORY;

            // If there is only one provider but multiple options, skip the categories menu
            // and directly open the options menu for this single provider.
            if (!this.routeToOptions(player, crate, singleType, close, callback)) {
                callback.cancel();
            }
            return;
        }

        // Multiple providers available: open the categories menu
        if (!this.openCategoriesMenu(player, crate, availableTypes, callback)) {
            callback.cancel();
        }
    }

    private boolean openCategoriesMenu(Player player,
                                       Crate crate,
                                       List<CostType<?>> availableTypes,
                                       ProcessCallback<CostTypeOption> callback) {

        // The menu will handle onAbort if the user closes the inventory
        BackwardNavigator close = BackwardNavigator.CLOSE_INVENTORY;

        Identifier crateId = crate.id();

        List<Identifier> typeIds = availableTypes.stream()
            .map(CostType::id)
            .toList();

        return this.handleFeedback(player, this.uiService.openCategoriesMenu(player,
            new CostCategoriesMenuContext(
                crateId, typeIds, close, callback::cancel, selectedTypeId -> {
                    CostType<?> selectedType = this.types.get(selectedTypeId);
                    if (selectedType == null) {
                        callback.cancel();
                        return;
                    }

                    BackwardNavigator backToCategories = user -> {
                        this.startSelection(player, crate, callback);
                    };

                    if (!this.routeToOptions(player, crate, selectedType, backToCategories, callback)) {
                        callback.cancel();
                    }
                })));
    }

    private <T extends CostOption> boolean routeToOptions(Player player,
                                                          Crate crate,
                                                          CostType<T> selectedType,
                                                          BackwardNavigator backwardNavigator,
                                                          ProcessCallback<CostTypeOption> callback) {

        List<T> options = this.costService.getAvailableOptions(crate, selectedType);

        return this.openOptionsMenu(player, crate, selectedType, options, backwardNavigator, callback);
    }

    private <T extends CostOption> boolean openOptionsMenu(Player player,
                                                           Crate crate,
                                                           CostType<T> selectedType,
                                                           List<T> options,
                                                           BackwardNavigator backwardNavigator,
                                                           ProcessCallback<CostTypeOption> callback) {
        Identifier crateId = crate.id();
        Identifier selectedTypeId = selectedType.id();
        List<String> optionIds = options
            .stream()
            .map(CostOption::getIdentifier)
            .toList();

        AtomicBoolean isChosen = new AtomicBoolean(false);

        return this.handleFeedback(player, this.uiService.openOptionsMenu(player, new CostOptionsMenuContext(
            crateId, selectedTypeId, optionIds, isChosen, backwardNavigator, callback::cancel, selectedOptionId -> {
                // Check if the player can afford the selected option
                // The batch amount is processed after the cost selection stage, so we check affordability for a single unit here.
                ActionResult affordance = this.costService.checkAffordance(player, crate, selectedType,
                    selectedOptionId, 1);
                if (!affordance.success()) {
                    this.handleFeedback(player, affordance);
                    callback.cancel();
                    return;
                }

                isChosen.set(true);
                player.closeInventory();

                callback.proceed(new DefaultCostTypeOption(selectedTypeId, selectedOptionId));
            }
        )));
    }
}
