package su.nightexpress.excellentcrates.reward.editor.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RewardCreationContext(ItemStack itemStack,
                                    boolean useItemReference,
                                    boolean addToGivenItems) {

}
