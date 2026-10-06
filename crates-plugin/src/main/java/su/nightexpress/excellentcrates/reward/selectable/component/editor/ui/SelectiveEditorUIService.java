package su.nightexpress.excellentcrates.reward.selectable.component.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.reward.selectable.component.editor.ui.menu.context.SelectiveEditorSettingsMenuContext;

@NullMarked
public class SelectiveEditorUIService {

    private final CoreUIService coreUI;

    public SelectiveEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openSettingsMenu(Player player, SelectiveEditorSettingsMenuContext context) {
        return coreUI.openMenu(player, SelectiveEditorUIKeys.SETTINGS, context);
    }
}
