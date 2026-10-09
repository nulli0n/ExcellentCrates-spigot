package su.nightexpress.excellentcrates.reward.editor.ui.menu;

import java.util.List;
import java.util.stream.IntStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.excellentcrates.reward.editor.lang.RewardEditorLang;
import su.nightexpress.excellentcrates.reward.editor.ui.RewardEditorUIController;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.ui.inventory.action.ActionContext;
import su.nightexpress.nightcore.ui.inventory.item.ItemState;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;
import su.nightexpress.nightcore.ui.inventory.menu.AbstractObjectMenu;
import su.nightexpress.nightcore.ui.inventory.viewer.ViewerContext;
import su.nightexpress.nightcore.util.bukkit.NightItem;
import su.nightexpress.nightcore.util.placeholder.CommonPlaceholders;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RewardPreviewMenu extends AbstractObjectMenu<RewardPreviewMenuContext> {

    private final RewardEditorUIController controller;

    public RewardPreviewMenu(CratesPlugin plugin, RewardEditorUIController controller) {
        super(plugin, MenuType.GENERIC_9X4, RewardEditorLang.UI_INVENTORY_PREVIEW_TITLE
            .text(), RewardPreviewMenuContext.class);
        this.controller = controller;
    }

    @Override
    public void defineDefaultLayout() {
        this.addBackgroundItem(Material.GRAY_STAINED_GLASS_PANE, IntStream.range(0, 27).toArray());
        this.addBackgroundItem(Material.BLACK_STAINED_GLASS_PANE, IntStream.range(27, 36).toArray());

        this.addBackButton(this::handleBack, 31);
    }

    @Override
    protected void onClick(ViewerContext context, InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int slot = event.getRawSlot();
        if (slot < inventory.getSize()) return;

        // Always reset click cooldown even if air clicked, so next quick click on item will count as well
        context.getViewer().setNextClickIn(0L);

        if (event.getClick() == ClickType.DOUBLE_CLICK) {
            ItemStack itemStack = event.getCurrentItem();
            if (itemStack == null || itemStack.getType().isAir()) return;

            RewardPreviewMenuContext menuContext = this.getObject(context);
            Reward reward = menuContext.rewardRef().get();
            if (reward == null) return;

            Player player = context.getPlayer();

            if (this.controller.onPreviewMenuIconClick(player, reward, itemStack)) {
                context.getViewer().refresh(); // Refresh on successful replacement only.
            }
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
        RewardPreviewMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) return;

        RewardPreview preview = reward.getPreview();
        ItemStack itemStack = preview.getIcon().getItemStack();

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.NAME_TAG)
                    .localized(RewardEditorLang.UI_INVENTORY_PREVIEW_BUTTON_NAME)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, preview::getName)
                    )
                )
                .action(this::handleRewardName)
                .build()
            )
            .slots(10)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.WRITABLE_BOOK)
                    .localized(RewardEditorLang.UI_INVENTORY_PREVIEW_BUTTON_LORE)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(CommonPlaceholders.GENERIC_VALUE, () -> {
                            return String.join(TagWrappers.BR, preview.getLore());
                        })
                    )
                )
                .action(this::handleRewardLore)
                .build()
            )
            .slots(12)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(itemStack == null ? NightItem.fromType(Material.ITEM_FRAME) : NightItem.fromItemStack(itemStack)
                    .localized(RewardEditorLang.UI_INVENTORY_PREVIEW_BUTTON_ICON)
                    .hideAllComponents()
                )
                .build()
            )
            .slots(14)
            .build()
        );

        items.add(MenuItem.button()
            .defaultState(ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_EYE)
                    .localized(RewardEditorLang.UI_INVENTORY_PREVIEW_BUTTON_INHERIT_ICON_META)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(true))
                    )
                )
                .condition(ctx -> preview.isInheritFromIcon())
                .action(this::handleRewardInheritFromIcon)
                .build()
            )
            .state("disabled", ItemState.builder()
                .icon(NightItem.fromType(Material.ENDER_PEARL)
                    .localized(RewardEditorLang.UI_INVENTORY_PREVIEW_BUTTON_INHERIT_ICON_META)
                    .hideAllComponents()
                    .replace(ctx -> ctx
                        .with(SharedPlaceholders.STATE, () -> CoreLang.STATE_ENABLED_DISALBED.get(false))
                    )
                )
                .condition(ctx -> !preview.isInheritFromIcon())
                .action(this::handleRewardInheritFromIcon)
                .build()
            )
            .slots(16)
            .build()
        );
    }

    @Override
    public void onReady(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    @Override
    public void onRender(ViewerContext context, InventoryView view, Inventory inventory) {

    }

    private void handleBack(ActionContext context) {
        RewardPreviewMenuContext menuContext = this.getObject(context);

        menuContext.moveBackward(context.getPlayer());
    }

    private void handleRewardName(ActionContext context) {
        RewardPreviewMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) {
            return;
        }

        Player player = context.getPlayer();
        RewardPreview preview = reward.getPreview();
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onPreviewMenuNameClick(player, reward, preview, refreshUI);
    }

    private void handleRewardLore(ActionContext context) {
        RewardPreviewMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) {
            return;
        }

        Player player = context.getPlayer();
        RewardPreview preview = reward.getPreview();
        Runnable refreshUI = () -> this.refresh(player);

        this.controller.onPreviewMenuLoreClick(player, reward, preview, refreshUI);
    }

    private void handleRewardInheritFromIcon(ActionContext context) {
        RewardPreviewMenuContext menuContext = this.getObject(context);
        Reward reward = menuContext.rewardRef().get();
        if (reward == null) {
            return;
        }

        Player player = context.getPlayer();
        RewardPreview preview = reward.getPreview();
        boolean newState = !preview.isInheritFromIcon();

        if (this.controller.onPreviewMenuAutoResolveClick(player, reward, newState)) {
            this.refresh(player);
        }
    }
}
