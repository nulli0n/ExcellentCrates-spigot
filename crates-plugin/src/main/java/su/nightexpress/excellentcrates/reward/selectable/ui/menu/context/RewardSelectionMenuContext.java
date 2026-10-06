package su.nightexpress.excellentcrates.reward.selectable.ui.menu.context;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.reward.selectable.SelectivePickContext;

@NullMarked
public record RewardSelectionMenuContext(CrateReference crateReference,
                                         SelectivePickContext pickContext,
                                         AtomicBoolean isCompleted,
                                         Runnable onAbort,
                                         Consumer<SelectivePickContext> onComplete) {

}
