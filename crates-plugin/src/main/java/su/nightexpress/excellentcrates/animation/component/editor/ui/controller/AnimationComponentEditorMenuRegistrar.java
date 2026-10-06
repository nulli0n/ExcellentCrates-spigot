package su.nightexpress.excellentcrates.animation.component.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIKeys;
import su.nightexpress.excellentcrates.animation.component.editor.ui.AnimationComponentEditorUIController;
import su.nightexpress.excellentcrates.animation.component.editor.ui.menu.AnimationComponentMenu;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.animation.AnimationRegistry;

@NullMarked
public class AnimationComponentEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                         plugin;
    private final CoreUIService                        coreUI;
    private final AnimationRegistry                    animations;
    private final AnimationComponentEditorUIController uiController;

    public AnimationComponentEditorMenuRegistrar(CratesPlugin plugin,
                                                 CoreUIService coreUI,
                                                 AnimationRegistry animations,
                                                 AnimationComponentEditorUIController uiController) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.animations = animations;
        this.uiController = uiController;
    }

    @Override
    public void start() {
        AnimationComponentMenu componentMenu = new AnimationComponentMenu(plugin, animations, uiController);

        componentMenu.load();

        this.coreUI.registerMenu(AnimationComponentEditorUIKeys.MENU_COMPONENT, componentMenu);
    }
}
