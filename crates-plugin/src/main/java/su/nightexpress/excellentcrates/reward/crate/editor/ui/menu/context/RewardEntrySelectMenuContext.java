package su.nightexpress.excellentcrates.reward.crate.editor.ui.menu.context;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.engine.ui.menu.BackwardSupport;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record RewardEntrySelectMenuContext(CrateReference crateRef,
                                           @Nullable RewardReference rewardRef,
                                           Consumer<Identifier> callback,
                                           BackwardNavigator backwardNavigator) implements BackwardSupport {

}
