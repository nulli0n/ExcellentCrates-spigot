package su.nightexpress.excellentcrates.reward.feature.commands.editor.ui;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorHook;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.RewardCommandsEditorService;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.context.CommandBundleContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.CommandsBundleDeleteDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsBundleDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsGiveModeDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.dialog.context.RewardCommandsIterationsDialogContext;
import su.nightexpress.excellentcrates.reward.feature.commands.editor.ui.menu.context.RewardCommandsMenuContext;

@NullMarked
public class RewardCommandsEditorUIController implements FeedbackHandler {

    private final RewardCommandsEditorService   editorService;
    private final RewardCommandsEditorUIService uiService;
    private final MessageDispatcher             dispatcher;

    public RewardCommandsEditorUIController(RewardCommandsEditorService editorService,
                                            RewardCommandsEditorUIService uiService,
                                            MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier rewardId, RewardEditorHook hook,
                                 BackwardNavigator backwardNavigator) {
        RewardCommandsMenuContext menuContext = new RewardCommandsMenuContext(rewardId, hook, backwardNavigator);

        this.handleFeedback(player, this.uiService.openCommandsMenu(player, menuContext));
    }

    public boolean onCommandsMenuAddClick(Player player, Identifier rewardId, RewardCommandPool bundle,
                                          RewardEditorHook hook) {
        return this.handleFeedback(player, this.editorService.addCommandBundle(hook, bundle));
    }

    public boolean onCommandsMenuStateClick(Player player, RewardEditorHook hook, boolean newState) {
        return this.handleFeedback(player, this.editorService.setCommandsState(hook, newState));
    }

    public void onCommandsMenuIterationsClick(Player player, Identifier rewardId, int currentIterations,
                                              RewardEditorHook hook,
                                              Runnable refreshUI) {
        RewardCommandsIterationsDialogContext dialogContext = new RewardCommandsIterationsDialogContext(
            rewardId,
            currentIterations,
            hook
        );

        this.handleFeedback(player, this.uiService.showCommandsIterationsDialog(player, dialogContext, refreshUI));
    }

    public void onCommandsMenuGiveModeClick(Player player, Identifier rewardId, RewardCommandExecutionMode currentMode,
                                            RewardEditorHook hook,
                                            Runnable refreshUI) {
        RewardCommandsGiveModeDialogContext dialogContext = new RewardCommandsGiveModeDialogContext(
            rewardId,
            currentMode,
            hook
        );

        this.handleFeedback(player, this.uiService.showCommandsGiveModeDialog(player, dialogContext, refreshUI));
    }

    public void onCommandsMenuBundleClick(Player player, Identifier rewardId, CommandBundleContext bundleContext,
                                          RewardEditorHook hook,
                                          Runnable refreshUI) {
        RewardCommandsBundleDialogContext dialogContext = new RewardCommandsBundleDialogContext(
            rewardId,
            bundleContext,
            hook
        );

        this.handleFeedback(player, this.uiService.showCommandsBundleDialog(player, dialogContext, refreshUI));
    }

    public void onCommandsMenuBundleDeleteClick(Player player, CommandBundleContext bundleContext,
                                                RewardEditorHook hook,
                                                Runnable refreshUI) {
        CommandsBundleDeleteDialogContext dialogContext = new CommandsBundleDeleteDialogContext(
            bundleContext,
            hook
        );

        this.handleFeedback(player, this.uiService.showCommandsBundleDeleteDialog(player, dialogContext, refreshUI));
    }

    public boolean onDialogCommandsIterationsConfirm(Player player, RewardEditorHook hook, int iterations) {
        return this.handleFeedback(player, this.editorService.setCommandsIterations(hook, iterations));
    }

    public boolean onDialogCommandsGiveModeConfirm(Player player, RewardEditorHook hook,
                                                   RewardCommandExecutionMode giveMode) {
        return this.handleFeedback(player, this.editorService.setCommandsGiveMode(hook, giveMode));
    }

    public boolean onDialogCommandsBundleConfirm(Player player, RewardEditorHook hook,
                                                 CommandBundleContext bundleContext) {
        return this.handleFeedback(player, this.editorService.updateCommandBundle(hook, bundleContext));
    }

    public boolean onDialogCommandsBundleDeleteConfirm(Player player, RewardEditorHook hook, UUID bundleId) {
        return this.handleFeedback(player, this.editorService.removeCommandBundle(hook, bundleId));
    }
}
