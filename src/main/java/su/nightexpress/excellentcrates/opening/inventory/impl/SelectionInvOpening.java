package su.nightexpress.excellentcrates.opening.inventory.impl;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.inventory.InvOpeningProvider;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.util.Lists;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class SelectionInvOpening extends InventoryOpening {

    private final Set<Integer> selectedSlots;

    public SelectionInvOpening(@NotNull CratesPlugin plugin,
                               @NotNull InvOpeningProvider config,
                               @NotNull InventoryView view,
                               @NotNull Player player,
                               @NotNull CrateSource source,
                               @Nullable CrateKey key) {
        super(plugin, config, view, player, source, key);
        this.selectedSlots = new HashSet<>();
    }

    @Override
    public int[] parseSlots(@NotNull String str) {
        return super.parseSlots(str
            .replace(Placeholders.SELECTED_SLOTS, this.serializeSlots(this.getSelectedSlots()))
            .replace(Placeholders.UNSELECTED_SLOTS, this.serializeSlots(this.getUnselectedSlots()))
        );
    }

    @NotNull
    private String serializeSlots(@NotNull Set<Integer> slots) {
        return slots.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    @Override
    public void launch() {
        super.launch();

        Inventory inventory = this.getInventory();

        for (int slot : this.config.getSelectionSlots()) {
            inventory.setItem(slot, null);
        }
    }

    @Override
    protected void onStart() {
        Inventory inventory = this.getInventory();
        ItemStack origin = this.config.getSelectionOriginIcon().getItemStack();

        for (int slot : this.config.getSelectionSlots()) {
            inventory.setItem(slot, origin);
        }

        super.onStart();
    }

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        super.onClick(event);
        if (this.isLaunched()) return;

        int slot = event.getRawSlot();
        Inventory inventory = this.getInventory();

        if (this.isSelectedSlot(slot)) {
            this.unselectSlot(slot);

            inventory.setItem(slot, this.config.getSelectionOriginIcon().getItemStack());
        }
        else if (Lists.contains(this.config.getSelectionSlots(), slot)) {
            this.selectSlot(slot);

            inventory.setItem(slot, this.config.getSelectionClickedIcon().getItemStack());

            if (!this.isLaunched() && this.isAllSlotsSelected()) {
                this.launch();
            }
        }
    }

    @Override
    protected void onInstaRoll() {
        this.selectedSlots.clear();

        for (int index = 0; index < this.config.getSelectionAmount(); index++) {
            this.selectSlot(this.config.getWinSlots()[index]);
        }
    }

    public boolean isAllSlotsSelected() {
        return this.selectedSlots.size() >= this.config.getSelectionAmount();
    }

    public void selectSlot(int slot) {
        this.selectedSlots.add(slot);
    }

    public void unselectSlot(int slot) {
        this.selectedSlots.remove(slot);
    }

    public boolean isSelectedSlot(int slot) {
        return this.selectedSlots.contains(slot);
    }

    @NotNull
    public Set<Integer> getSelectedSlots() {
        return this.selectedSlots;
    }

    @NotNull
    public Set<Integer> getUnselectedSlots() {
        return Arrays.stream(this.config.getSelectionSlots()).filter(slot -> !this.isSelectedSlot(slot)).boxed().collect(Collectors.toSet());
    }
}
