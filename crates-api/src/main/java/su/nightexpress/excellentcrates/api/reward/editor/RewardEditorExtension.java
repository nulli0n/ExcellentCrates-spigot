package su.nightexpress.excellentcrates.api.reward.editor;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.nightcore.ui.inventory.item.MenuItem;

@NullMarked
public interface RewardEditorExtension extends Identifiable {

    MenuItem createButton(Reward reward, RewardEditorHook hook, BackwardNavigator backwardNavigator, int slot);

}
