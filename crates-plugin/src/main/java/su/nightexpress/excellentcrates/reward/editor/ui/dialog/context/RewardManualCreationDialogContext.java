package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;

@NullMarked
public record RewardManualCreationDialogContext(RewardComponentHook hook,
                                                CrateReference crateRef,
                                                @Nullable String name,
                                                ItemStack item) {

}
