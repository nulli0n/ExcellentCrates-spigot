package su.nightexpress.excellentcrates.reward.editor.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record RewardCreateContext(@Nullable String name, ItemStack item) {

}
