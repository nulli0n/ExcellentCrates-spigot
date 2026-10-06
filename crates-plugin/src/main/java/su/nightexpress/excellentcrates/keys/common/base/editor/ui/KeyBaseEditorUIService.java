package su.nightexpress.excellentcrates.keys.common.base.editor.ui;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.keys.common.base.editor.ui.menu.context.KeyBaseMainMenuContext;

@NullMarked
public class KeyBaseEditorUIService {

    private final CoreUIService coreUI;

    public KeyBaseEditorUIService(CoreUIService coreUI) {
        this.coreUI = coreUI;
    }

    public ActionResult openBaseMenu(Player player, KeyBaseMainMenuContext context) {
        return this.coreUI.openMenu(player, KeyBaseEditorUIKeys.MAIN_MENU, context);
    }
}
