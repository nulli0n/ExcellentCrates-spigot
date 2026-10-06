package su.nightexpress.excellentcrates.reward.items.editor.ui.dialog.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardItemAddDialogContext(RewardReference rewardRef, RewardEditorHook hook, ItemStack itemStack) {

}
