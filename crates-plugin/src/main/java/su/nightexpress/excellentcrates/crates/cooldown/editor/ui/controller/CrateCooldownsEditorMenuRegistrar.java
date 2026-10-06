package su.nightexpress.excellentcrates.crates.cooldown.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateResolver;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIKeys;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.CrateCooldownsEditorUIController;
import su.nightexpress.excellentcrates.crates.cooldown.editor.ui.menu.CrateCooldownsMenu;

@NullMarked
public class CrateCooldownsEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                     plugin;
    private final CoreUIService                    coreUI;
    private final CrateResolver                    crateResolver;
    private final CrateCooldownsEditorUIController uiController;

    public CrateCooldownsEditorMenuRegistrar(CratesPlugin plugin,
                                             CoreUIService coreUI,
                                             CrateResolver crateResolver,
                                             CrateCooldownsEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.crateResolver = crateResolver;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        CrateCooldownsMenu cooldownsMenu = new CrateCooldownsMenu(this.plugin, this.crateResolver, this.uiController);

        cooldownsMenu.load();

        this.coreUI.registerMenu(CrateCooldownsEditorUIKeys.MENU_COOLDOWNS, cooldownsMenu);
    }

}
