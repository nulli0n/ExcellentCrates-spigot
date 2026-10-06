package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record RewardDeletionDialogContext(Identifier rewardId, ItemStack currentIcon) {

}
