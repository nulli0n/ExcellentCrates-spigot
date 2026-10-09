package su.nightexpress.excellentcrates.reward.editor.ui.menu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.extension.PositionedExtension;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorExtension;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.preview.RewardPreviewService;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class RewardOptionsMenu extends AbstractObjectMenu<RewardOptionsMenuContext> {

    private static final int[] EXTENSION_SLOTS = {
        //10, 11, 12, 13, 14, 15, 16,
        19, 20, 21, 22, 23, 24, 25,
        28, 29, 30, 31, 32, 33, 34,
        37, 38, 39, 40, 41, 42, 43
    };

    private final RewardPreviewService                previewService;
    private final RewardEditorUIController            controller;
    private final TinyRegistry<RewardEditorExtension> extensions;

    public RewardOptionsMenu(CratesPlugin plugin,
                             RewardPreviewService previewService,
                             RewardEditorUIController controller,
                             TinyRegistry<RewardEditorExtension> extensions) {
        super(plugin, MenuType.GENERIC_9X6, RewardEditorLang.UI_INVENTORY_OPTIONS_TITLE
            .text(), RewardOptionsMenuContext.class);
        this.previewService = previewService;
        this.controller = controller;
        this.extensions = extensions;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 45).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(45, 54).toArray());

        this.addBackButton(this::handleBack, 45);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (event.getRawSlot() >= inventory.getSize()) {
            event.setCancelled(false);
        }
    }

    @Override
    protected void onClose(ViewerContext context, InventoryCloseEvent event) {

    }

    @Override
    protected void onDrag(ViewerContext context, InventoryDragEvent event) {

    }

    @Override
    protected void onLoad(FileConfig config) {

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
        RewardOptionsMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        items.addAll(this.renderDefaultButtons(crate, reward));
        items.addAll(this.renderExtensions(player, reward, inventory, menuContext));
    }

    private List<MenuItem> renderDefaultButtons(Crate crate, Reward reward) {
        List<MenuItem> items = new ArrayList<>();

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(this.previewService.createPreviewIcon(reward)
                    .localized(RewardEditorLang.UI_INVENTORY_OPTIONS_BUTTON_PREVIEW)
                    .hideAllComponents()
                )
                .action(this::handlePreview)
                .build()
            )
            .slots(4)
            .build()
        );

        NightItem weightIcon = NightItem.fromType(Material.ANVIL)
            .hideAllComponents()
            .localized(RewardEditorLang.UI_INVENTORY_OPTIONS_BUTTON_WEIGHT);

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(this.previewService.addPlaceholders(weightIcon, crate, reward))
                .action(actionContext -> this.handleWeight(actionContext, reward.getBase().getWeight()))
                .build()
            )
            .slots(10)
            .build()
        );

        items.add(MenuItem.custom()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.BARRIER)
                    .localized(RewardEditorLang.UI_INVENTORY_OPTIONS_BUTTON_DELETE)
                    .hideAllComponents()
                )
                .action(this::handleDelete)
                .build()
            )
            .slots(53)
            .build()
        );

        return items;
    }

    private List<MenuItem> renderExtensions(Player player, Reward reward, Inventory inventory,
                                            RewardOptionsMenuContext menuContext) {
        List<MenuItem> items = new ArrayList<>();

        RewardEditorHook hook = action -> this.controller.onExtensionModify(player, reward.id(), action);
        BackwardNavigator navigator = user -> this.controller.onExtensionMoveBackward(user, menuContext);

        List<RewardEditorExtension> unpositionedExtensions = new ArrayList<>();
        Set<Integer> occupiedSlots = new HashSet<>();

        this.extensions.forEach(extension -> {
            if (extension instanceof PositionedExtension positioned) {
                int slot = positioned.getFixedSlot();
                if (slot < 0 || slot >= inventory.getSize() || occupiedSlots.contains(slot)) {
                    unpositionedExtensions.add(extension);
                    return;
                }

                items.add(extension.createButton(reward, hook, navigator, slot));
                occupiedSlots.add(slot);
                return;
            }

            unpositionedExtensions.add(extension);
        });

        unpositionedExtensions.sort(Comparator.comparing(RewardEditorExtension::idString));

        for (int index = 0; index < unpositionedExtensions.size(); index++) {
            if (index >= EXTENSION_SLOTS.length) break;

            RewardEditorExtension extension = unpositionedExtensions.get(index);
            int slot = EXTENSION_SLOTS[index];

            items.add(extension.createButton(reward, hook, navigator, slot));
        }

        return items;
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardOptionsMenuContext menuContext = this.getObject(context);
        menuContext.moveBackward(context.getPlayer());
    }

    private void handleWeight(ActionContext context, double currentWeight) {
        Player player = context.getPlayer();
        RewardOptionsMenuContext menuContext = this.getObject(context);
        Runnable refreshUI = () -> context.getViewer().refresh();

        this.controller.onOptionsWeightClick(player, menuContext, currentWeight, refreshUI);
    }

    private void handlePreview(ActionContext context) {
        Player player = context.getPlayer();
        RewardOptionsMenuContext menuContext = this.getObject(context);

        this.controller.onOptionsMenuPreviewClick(player, menuContext);
    }

    private void handleDelete(ActionContext context) {
        Player player = context.getPlayer();
        RewardOptionsMenuContext menuContext = this.getObject(context);

        Crate crate = menuContext.crateRef().get();
        if (crate == null) return;

        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        NightItem preview = this.previewService.createPreviewIconWithAllPlaceholders(crate, reward);
        Runnable callback = () -> menuContext.moveBackward(player);

        this.controller.onOptionsDeleteClick(player, menuContext, preview, callback);
    }
}
