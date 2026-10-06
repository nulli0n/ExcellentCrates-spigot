package su.nightexpress.excellentcrates.crates.cooldown.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.dialog.context.CrateCooldownsSettingsDialogContext;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.menu.context.CrateCooldownsMenuContext;

@NullMarked
public class CrateCooldownsEditorUIService {

    private final CoreUIService coreUI;

    public CrateCooldownsEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openCooldownsMenu(Player player, CrateCooldownsMenuContext context) {
        return this.coreUI.openMenu(player, CrateCooldownsEditorUIKeys.MENU_COOLDOWNS, context);
    }

    public ActionResult showCooldownSettingsDialog(Player player, CrateCooldownsSettingsDialogContext context,
                                                   Runnable callback) {
        return this.coreUI.showDialog(player, CrateCooldownsEditorUIKeys.DIALOG_COOLDOWN_SETTINGS, context,
            callback);
    }
}
