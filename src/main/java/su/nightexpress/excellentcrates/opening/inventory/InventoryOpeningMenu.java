package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.menu.MenuOptions;
import su.nightexpress.nightcore.menu.MenuSize;
import su.nightexpress.nightcore.menu.MenuViewer;
import su.nightexpress.nightcore.menu.impl.ConfigMenu;
import su.nightexpress.nightcore.menu.item.ItemHandler;
import su.nightexpress.nightcore.menu.item.MenuItem;

import java.util.ArrayList;
import java.util.List;

import static su.nightexpress.excellentcrates.Placeholders.CRATE_NAME;
import static su.nightexpress.nightcore.util.text.tag.Tags.BLACK;

public class InventoryOpeningMenu extends ConfigMenu<CratesPlugin> {

    private final ItemHandler startHandler;
    private final ItemHandler selectHandler;
    private InventoryOpeningConfig openingConfig;

    public InventoryOpeningMenu(@NotNull CratesPlugin plugin, @NotNull FileConfig config) {
        super(plugin, config);

        this.addHandler(this.startHandler = new ItemHandler("start", (viewer, event) -> {
            InventoryOpening opening = this.getOpening(viewer.getPlayer());
            if (opening == null || opening.isStarted() || opening.isCompleted()) return;

            if (this.openingConfig.getMode() == InventoryOpening.Mode.SELECTION) {
                if (this.openingConfig.isAutoRun() || !opening.isAllSlotsSelected() || opening.getSelectedSlots().isEmpty()) {
                    return;
                }
            }

            opening.start();
            this.flush(viewer);
        }));

        this.addHandler(this.selectHandler = new ItemHandler("select", (viewer, event) -> {
            if (this.openingConfig.getMode() != InventoryOpening.Mode.SELECTION) return;

            InventoryOpening opening = this.getOpening(viewer.getPlayer());
            if (opening == null || opening.isStarted() || opening.isCompleted()) return;

            int slot = event.getRawSlot();
            if (opening.isSelectedRewardSlot(slot)) {
                opening.unselectRewardSlot(slot);
            } else {
                if (!opening.isAllSlotsSelected()) {
                    opening.selectRewardSlot(slot);
                }
            }
            this.flush(viewer);
        }));

        this.load();

        this.getItems().forEach(menuItem -> {
            if (menuItem.getHandler() == this.startHandler || menuItem.getHandler() == this.selectHandler) {
                menuItem.getOptions().addVisibilityPolicy(viewer -> {
                    InventoryOpening opening = this.getOpening(viewer.getPlayer());
                    if (opening == null) return false;

                    return !opening.isStarted() && !opening.isCompleted();
                });
            }
        });
    }

    @NotNull
    public InventoryOpeningConfig getOpeningConfig() {
        return openingConfig;
    }

    @Nullable
    private InventoryOpening getOpening(@NotNull Player player) {
        Opening opening = this.plugin.getOpeningManager().getOpeningData(player);
        if (!(opening instanceof InventoryOpening inventoryOpening)) return null;

        return inventoryOpening;
    }

    @Override
    protected void onPrepare(@NotNull MenuViewer viewer, @NotNull MenuOptions options) {
        InventoryOpening opening = this.getOpening(viewer.getPlayer());
        if (opening == null) return;

        Crate crate = opening.getCrate();

        options.setTitle(crate.replacePlaceholders().apply(options.getTitle()));
    }

    @Override
    protected void onReady(@NotNull MenuViewer viewer, @NotNull Inventory inventory) {
        InventoryOpening opening = this.getOpening(viewer.getPlayer());
        if (opening == null) return;

        if (!opening.isStarted() && this.openingConfig.getMode() == InventoryOpening.Mode.SELECTION) {
            for (int slot = 0; slot < inventory.getSize(); slot++) {
                MenuItem menuItem = this.getItem(viewer, slot);
                if (menuItem == null || menuItem.getHandler() != this.selectHandler) continue;

                if (opening.isSelectedRewardSlot(slot)) {
                    inventory.setItem(slot, this.openingConfig.getSelectedIcon());
                }
            }
        }
    }

    @Override
    public void onClose(@NotNull MenuViewer viewer, @NotNull InventoryCloseEvent event) {
        InventoryOpening opening = this.getOpening(viewer.getPlayer());
        if (opening == null) {
            super.onClose(viewer, event);
            return;
        }

        if (!opening.isCompleted()) {
            if (opening.canSkip()) {
                opening.instaRoll();
            } else /*if (opening.hasRewardAttempts())*/ {
                opening.setPopupNextTick(true);
            }
            //this.runNextTick(() -> viewer.getPlayer().openInventory(opening.getInventory()));
        } else {
            opening.setCloseDelay(0);
            opening.stop();
            //super.onClose(viewer, event);
        }
    }

    @Override
    @NotNull
    protected MenuOptions createDefaultOptions() {
        return new MenuOptions(BLACK.enclose("Opening " + CRATE_NAME), MenuSize.CHEST_27);
    }

    @Override
    @NotNull
    protected List<MenuItem> createDefaultItems() {
        return new ArrayList<>();
    }

    @Override
    protected void loadAdditional() {
        this.openingConfig = InventoryOpeningConfig.read(this.cfg);
    }
}
