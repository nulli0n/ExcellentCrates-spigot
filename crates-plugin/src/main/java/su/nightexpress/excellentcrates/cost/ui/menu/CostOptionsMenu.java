package su.nightexpress.excellentcrates.cost.ui.menu;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.cost.type.CostDisplayInfo;
import su.nightexpress.excellentcrates.api.cost.type.CostDisplayProvider;
import su.nightexpress.excellentcrates.api.cost.type.CostLogicProvider;
import su.nightexpress.excellentcrates.api.cost.type.CostOption;
import su.nightexpress.excellentcrates.api.cost.type.CostType;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.cost.lang.CostLang;
import su.nightexpress.excellentcrates.cost.ui.menu.context.CostOptionsMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.item.populator.SlotPattern;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;

@NullMarked
public class CostOptionsMenu extends AbstractObjectMenu<CostOptionsMenuContext> {

    private static final SlotPattern DEFAULT_SLOT_PATTERN = new SlotPattern()
        .with(1, 13)
        .with(2, 12, 14)
        .with(3, 11, 13, 15)
        .with(4, 10, 12, 14, 16)
        .with(5, 11, 12, 13, 14, 15);

    private static final Logger LOGGER = LoggerFactory.getLogger(CostOptionsMenu.class);

    private final CrateResolver                     crateResolver;
    private final IdentifiableRegistry<CostType<?>> costTypes;

    private SlotPattern slotPattern = DEFAULT_SLOT_PATTERN;

    public CostOptionsMenu(CratesPlugin plugin,
                           CrateResolver crateResolver,
                           IdentifiableRegistry<CostType<?>> costTypes) {
        super(plugin, MenuType.GENERIC_9X4, CostLang.UI_INVENTORY_OPTIONS_TITLE.text(), CostOptionsMenuContext.class);
        this.crateResolver = crateResolver;
        this.costTypes = costTypes;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 27);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {
        CostOptionsMenuContext menuContext = this.getObject(context);
        if (!menuContext.isChosen().get()) {
            menuContext.onAbort().run();
        }
    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {
        this.slotPattern = config.getOrSet("cost.slot_by_count", ConfigCodecs.SLOT_PATTERN, DEFAULT_SLOT_PATTERN);
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        Player player = context.getPlayer();
        CostOptionsMenuContext menuContext = this.getObject(context);

        Crate crate = this.crateResolver.resolveCrate(menuContext.crateId());
        if (crate == null) {
            LOGGER.warn("Crate not found: {}", menuContext.crateId());
            return;
        }

        items.addAll(this.renderCostOptions(player, menuContext, crate));
    }

    private List<MenuItem> renderCostOptions(Player player, CostOptionsMenuContext menuContext, Crate crate) {
        Identifier costTypeId = menuContext.costType();
        CostType<?> costType = this.costTypes.get(costTypeId);
        if (costType == null) {
            LOGGER.warn("Cost type not found: {}", costTypeId);
            return List.of();
        }

        List<String> costOptions = menuContext.costOptions();
        List<MenuItem> items = new ArrayList<>();

        int[] slots = this.slotPattern.getSlots(costOptions.size());
        for (int index = 0; index < costOptions.size(); index++) {
            String option = costOptions.get(index);
            int slot = slots[index];
            MenuItem item = this.renderCostOption(player, crate, costType, option, slot);
            if (item != null) {
                items.add(item);
            }
        }

        return items;
    }

    private <T extends CostOption> @Nullable MenuItem renderCostOption(Player player,
                                                                       Crate crate,
                                                                       CostType<T> costType,
                                                                       String optionId,
                                                                       int slot) {

        CostDisplayProvider<T> display = costType.getDisplay();
        CostLogicProvider<T> logic = costType.getLogic();

        T option = logic.getOptionById(crate, optionId);
        if (option == null) {
            LOGGER.warn("Cost option '{}' not found in '{}' cost type.", optionId, costType.getId());
            return null;
        }

        CostDisplayInfo displayInfo = display.getOptionDisplay(crate, option);

        return MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(displayInfo.icon()
                    .hideAllComponents()
                    .localized(CostLang.UI_INVENTORY_OPTIONS_BUTTON_OPTION)
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_NAME, () -> displayInfo.name())
                        .with(CommonPlaceholders.GENERIC_DESCRIPTION, () -> {
                            return String.join("\n", displayInfo.lore());
                        })
                        .with(SharedPlaceholders.BALANCE, () -> {
                            return display.formatBalance(logic.getBalance(player, crate, option), option);
                        })
                        .with(SharedPlaceholders.AVAILABLE, () -> {
                            return NumberUtil.format(logic.getMaxAffordableOpens(player, crate, option));
                        })
                    )
                )
                .action(actionContext -> this.onCostChoice(actionContext, optionId))
                .build()
            )
            .slots(slot)
            .build();
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        CostOptionsMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void onCostChoice(ActionContext context, String costKey) {
        CostOptionsMenuContext menuContext = this.getObject(context);

        menuContext.onChoice().accept(costKey);
    }
}
