package su.nightexpress.excellentcrates.preview.inventory.menu;

import java.util.Arrays;

import su.nightexpress.nightcore.util.bukkit.NightItem;

public class InventoryButton {

    private final NightItem icon;
    private final int[]     slots;

    public InventoryButton(NightItem icon, int... slots) {
        this.icon = icon;
        this.slots = slots;
    }

    public NightItem getIcon() {
        return icon.copy();
    }

    public int[] getSlots() {
        return Arrays.copyOf(this.slots, this.slots.length);
    }
}
