package su.nightexpress.excellentcrates.util;

import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public enum ClickType {

    LEFT, RIGHT, SHIFT_LEFT, SHIFT_RIGHT;

    @NotNull
    private ClickType shifted(boolean shift) {
        if (!shift) return this;

        return this == LEFT ? SHIFT_LEFT : this == RIGHT ? SHIFT_RIGHT : this;
    }

    @NotNull
    public static ClickType from(@NotNull InventoryClickEvent event) {
        return (event.isRightClick() ? RIGHT : LEFT).shifted(event.isShiftClick());
        /*if (event.isShiftClick()) {
            if (event.isLeftClick()) {
                return SHIFT_LEFT;
            }
            return SHIFT_RIGHT;
        }
        if (event.isRightClick()) {
            return RIGHT;
        }
        return LEFT;*/
    }

    @NotNull
    public static ClickType from(@NotNull Action action, boolean shift) {
        boolean isRight = action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;

        return (isRight ? RIGHT : LEFT).shifted(shift);
    }
}
