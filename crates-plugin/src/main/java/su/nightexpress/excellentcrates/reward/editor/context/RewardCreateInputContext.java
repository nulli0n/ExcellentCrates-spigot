package su.nightexpress.excellentcrates.reward.editor.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record RewardCreateInputContext(String name,
                                       ItemStack itemStack,
                                       boolean useItemReference,
                                       boolean setItemContent) {

}
