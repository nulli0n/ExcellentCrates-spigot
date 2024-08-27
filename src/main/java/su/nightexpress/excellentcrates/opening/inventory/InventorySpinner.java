package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.opening.AbstractSpinner;
import su.nightexpress.excellentcrates.opening.spinner.SpinMode;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class InventorySpinner extends AbstractSpinner {

    protected final InventoryOpening opening;
    protected final SpinMode mode;
    protected final int[] slots;

    public InventorySpinner(@NotNull CratesPlugin plugin,
                            @NotNull String id,
                            @NotNull InventoryOpening opening,
                            @NotNull SpinMode mode, int[] slots) {
        super(plugin, id);
        this.opening = opening;
        this.mode = mode;
        this.slots = slots;
    }

    @NotNull
    public abstract ItemStack createItem();

    @NotNull
    @Override
    public InventoryOpening getOpening() {
        return this.opening;
    }

    @Override
    protected void onSpin() {
        switch (this.mode) {
            case SEQUENTAL -> this.spinSequental();
            case INDEPENDENT -> this.spinIndependent();
            case SYNCRHONIZED -> this.spinSynchronized();
            case RANDOM -> this.spinRandom();
        }
    }

    private boolean isOutOfBounds(int slot) {
        return slot < 0 || slot >= this.opening.getInventory().getSize();
    }

    private void spinSequental() {
        ItemStack item = this.createItem();
        if (item.getType().isAir()) return;

        Inventory inventory = this.opening.getInventory();
        for (int index = this.slots.length - 1; index > -1; index--) {
            int slot = slots[index];
            if (this.isOutOfBounds(slot)) continue;

            if (index == 0) {
                inventory.setItem(slot, item);
            } else {
                int previousSlot = slots[index - 1];
                inventory.setItem(slot, this.opening.getInventory().getItem(previousSlot));
            }
        }
    }

    private void spinIndependent() {
        Inventory inventory = this.opening.getInventory();
        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            ItemStack item = this.createItem();

            inventory.setItem(slot, item);
        }
    }

    private void spinSynchronized() {
        ItemStack item = this.createItem();

        Inventory inventory = this.opening.getInventory();
        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            inventory.setItem(slot, item);
        }
    }

    private void spinRandom() {
        List<Integer> slots = new ArrayList<>(IntStream.of(this.slots).boxed().toList());
        int roll = Rnd.get(slots.size() + 1);
        if (roll <= 0) return;

        Inventory inventory = this.opening.getInventory();
        while (roll > 0 && !slots.isEmpty()) {
            int slot = slots.remove(Rnd.get(slots.size()));

            if (!this.isOutOfBounds(slot)) {
                ItemStack item = this.createItem();
                inventory.setItem(slot, item);
            }

            roll--;
        }
    }
}
