package su.nightexpress.excellentcrates.util;

import java.util.function.Predicate;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class InventoryUtils {

    private InventoryUtils() {
    }

    public static int countInventorySpace(Player player, ItemStack itemToCount) {
        int space = 0;
        int maxStackSize = Math.min(player.getInventory().getMaxStackSize(), itemToCount.getMaxStackSize());
        for (ItemStack item : player.getInventory().getStorageContents()) {
            if (item == null || item.getType().isAir()) {
                space += maxStackSize;
            }
            else if (item.isSimilar(itemToCount)) {
                space += Math.max(0, maxStackSize - item.getAmount());
            }
        }
        return space;
    }

    public static int countInventoryAmount(Player player, Predicate<ItemStack> predicate) {
        int balance = 0;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null) continue;

            if (predicate.test(item)) {
                balance += item.getAmount();
            }
        }

        return balance;
    }

    public static int removeFromInventory(Player player, Predicate<ItemStack> predicate, int absAmount) {
        int removed = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType().isAir()) continue;

            if (!predicate.test(itemStack)) continue;

            int toRemove = Math.min(itemStack.getAmount(), absAmount - removed);
            itemStack.setAmount(itemStack.getAmount() - toRemove);
            removed += toRemove;

            if (removed >= absAmount) {
                break;
            }
        }

        return removed;
    }
}
