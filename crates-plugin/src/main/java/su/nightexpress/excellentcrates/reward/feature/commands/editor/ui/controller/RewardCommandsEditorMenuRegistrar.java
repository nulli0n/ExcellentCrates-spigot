package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.StartupComponent;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIController;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.RewardCommandsEditorUIKeys;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.RewardCommandsMenu;

@NullMarked
public class RewardCommandsEditorMenuRegistrar implements StartupComponent {

    private final CratesPlugin                     plugin;
    private final CoreUIService                    coreUI;
    private final RewardRegistry                   registry;
    private final RewardCommandsEditorUIController controller;

    public RewardCommandsEditorMenuRegistrar(CratesPlugin plugin,
                                             CoreUIService coreUI,
                                             RewardRegistry registry,
                                             RewardCommandsEditorUIController controller) {
        this.plugin = plugin;
        this.coreUI = coreUI;
        this.registry = registry;
        this.controller = controller;
    }

    @Override
    public void start() {
        RewardCommandsMenu menu = new RewardCommandsMenu(this.plugin, this.registry, this.controller);

        menu.load();

        this.coreUI.registerMenu(RewardCommandsEditorUIKeys.MENU_COMMANDS, menu);
    }

}
