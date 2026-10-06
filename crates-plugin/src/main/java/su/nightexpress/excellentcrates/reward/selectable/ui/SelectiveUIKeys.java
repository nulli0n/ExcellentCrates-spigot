package su.nightexpress.excellentcrates.reward.selectable.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.selectable.ui.menu.context.RewardSelectionMenuContext;

@NullMarked
public final class SelectiveUIKeys {

    public static final MenuKey<RewardSelectionMenuContext> MENU_SELECTION = MenuKey.of("rewards.selective.selection");

    private SelectiveUIKeys() {
    }
}
