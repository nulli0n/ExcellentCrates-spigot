package su.nightexpress.excellentcrates.reward.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.reward.editor.context.RewardCreateContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.PreviewDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.dialog.context.RewardDeletionDialogContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.BrowseMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardOptionsMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.menu.context.RewardPreviewMenuContext;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.EditorPreferences;
import su.nightexpress.excellentcrates.reward.editor.ui.preferences.PreferencesSessionManager;

@NullMarked
public class RewardEditorUIService {

    private final CoreUIService             coreUI;
    private final PreferencesSessionManager sessionManager;

    public RewardEditorUIService(CoreUIService coreUI, PreferencesSessionManager sessionManager) {
        this.coreUI = coreUI;
        this.sessionManager = sessionManager;
    }

    public ActionResult openCreationDialog(Player player, RewardCreateContext context, Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_CREATION, context, callback);
    }

    public ActionResult openBrowseMenu(Player player, BackwardNavigator navigator) {
        EditorPreferences preferences = this.sessionManager.getPreferencesOrCreate(player.getUniqueId());
        BrowseMenuContext menuContext = new BrowseMenuContext(preferences, navigator);

        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_BROWSE, menuContext);
    }

    public ActionResult openOptionsMenu(Player player, Identifier rewardId, BackwardNavigator navigator) {
        RewardOptionsMenuContext menuContext = new RewardOptionsMenuContext(rewardId, navigator);
        return this.openOptionsMenu(player, menuContext);
    }

    public ActionResult openOptionsMenu(Player player, RewardOptionsMenuContext context) {
        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_OPTIONS, context);
    }

    public ActionResult openPreviewMenu(Player player, RewardPreviewMenuContext context) {
        return this.coreUI.openMenu(player, RewardEditorUIKeys.MENU_PREVIEW, context);
    }

    public ActionResult showDeletionDialog(Player player, RewardDeletionDialogContext dialogContext,
                                           Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_DELETION, dialogContext, callback);
    }

    public ActionResult showPreviewNameDialog(Player player, PreviewDialogContext dialogContext, Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_PREVIEW_NAME, dialogContext, callback);
    }

    public ActionResult showPreviewLoreDialog(Player player, PreviewDialogContext dialogContext, Runnable callback) {
        return this.coreUI.showDialog(player, RewardEditorUIKeys.DIALOG_PREVIEW_LORE, dialogContext, callback);
    }
}
