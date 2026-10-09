package su.nightexpress.excellentcrates.reward.editor.ui.dialog.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.reward.crate.component.editor.RewardComponentHook;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public record RewardDeletionDialogContext(RewardComponentHook hook,
                                          CrateReference crateRef,
                                          RewardReference rewardRef,
                                          NightItem icon) {

}
