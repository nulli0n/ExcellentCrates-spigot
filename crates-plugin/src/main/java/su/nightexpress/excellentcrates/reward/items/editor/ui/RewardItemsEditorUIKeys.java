package su.nightexpress.excellentcrates.reward.items.editor.ui;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.ui.menu.MenuKey;
import su.nightexpress.excellentcrates.reward.items.editor.ui.dialog.context.RewardItemAddDialogContext;
import su.nightexpress.excellentcrates.reward.items.editor.ui.menu.context.RewardItemsMenuContext;
import su.nightexpress.nightcore.ui.dialog.wrap.DialogKey;

@NullMarked
public final class RewardItemsEditorUIKeys {

    public static final MenuKey<RewardItemsMenuContext> MENU_ITEMS = MenuKey.of(
        "rewards.items.editor.items"
    );

    public static final DialogKey<RewardItemAddDialogContext> DIALOG_ADD_ITEM = new DialogKey<>(
        "rewards.items.editor.add_item"
    );

    private RewardItemsEditorUIKeys() {
    }
}
