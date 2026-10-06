package su.nightexpress.excellentcrates.crates.batch.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.crates.batch.ui.menu.context.BatchAmountSelectionMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class BatchAmountSelectionMenu extends AbstractObjectMenu<BatchAmountSelectionMenuContext> {

    private static final int[] DEFAULT_AMOUNT_SLOTS = {2, 3, 4, 5, 6, 11, 12, 13, 14, 15, 20, 21, 22, 23, 24};

    private static final NightItem DEFAULT_AMOUNT_ITEM = NightItem.fromType(Material.LIME_STAINED_GLASS_PANE)
        .setDisplayName(TagWrappers.GREEN.and(TagWrappers.BOLD).wrap("OPEN " + CommonPlaceholders.GENERIC_AMOUNT + "X"))
        .setLore(List.of(
            TagWrappers.GRAY.wrap("Click to select.")
        ));

    private static final boolean DEFAULT_AMOUNT_INDICATOR = true;

    private int[]     amountSlots     = DEFAULT_AMOUNT_SLOTS;
    private NightItem amountItem      = DEFAULT_AMOUNT_ITEM;
    private boolean   amountIndicator = DEFAULT_AMOUNT_INDICATOR;

    public BatchAmountSelectionMenu(CratesPlugin plugin) {
        super(plugin, MenuType.GENERIC_9X3, "How many crates to open?", BatchAmountSelectionMenuContext.class);
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, DEFAULT_AMOUNT_SLOTS);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {

    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {
        BatchAmountSelectionMenuContext menuContext = this.getObject(context);
        if (!menuContext.isChosen().get()) {
            menuContext.onAbort().run();
        }
    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {
        this.amountSlots = config.getOrSet("batch.amount_slots", ConfigCodecs.INT_ARRAY, DEFAULT_AMOUNT_SLOTS);
        this.amountItem = config.getOrSet("batch.amount_item", ConfigCodecs.NIGHT_ITEM, DEFAULT_AMOUNT_ITEM);
        this.amountIndicator = config.getOrSet("batch.amount_indicator", ConfigCodecs.BOOLEAN,
            DEFAULT_AMOUNT_INDICATOR
        );
    }

    @Override
    public void registerActions() {

    }

    @Override
    public void registerConditions() {

    }

    @Override
    public void onPrepare(ViewerContext context, InventoryView view, Inventory inventory, List<MenuItem> items) {
        BatchAmountSelectionMenuContext menuContext = this.getObject(context);

        int maxAllowed = menuContext.maxAllowed();

        List<Integer> amounts = IntStream.rangeClosed(1, maxAllowed)
            .boxed()
            .toList();

        for (int index = 0; index < amounts.size(); index++) {
            if (index >= this.amountSlots.length) break;

            int amount = amounts.get(index);
            int slot = this.amountSlots[index];

            NightItem amountItem = this.amountItem.copy();
            if (this.amountIndicator) {
                amountItem.setAmount(amount);
            }

            items.add(MenuItem.custom()
                .defaultState(ItemState.builder()
                    .icon(amountItem
                        .hideAllComponents()
                        .replace(ctx -> ctx
                            .with(CommonPlaceholders.GENERIC_AMOUNT, () -> String.valueOf(amount))
                        ))
                    .action(ctx -> this.handleAmountSelection(ctx, amount))
                    .build()
                )
                .slots(slot)
                .build()
            );
        }
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleAmountSelection(ActionContext context, int amount) {
        BatchAmountSelectionMenuContext menuContext = this.getObject(context);

        menuContext.onSelect().accept(amount);
    }
}
