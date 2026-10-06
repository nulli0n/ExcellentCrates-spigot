package su.nightexpress.excellentcrates.crates.hologram.editor.ui;

import java.util.List;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.editor.CrateEditorHook;
import su.nightexpress.excellentcrates.api.crate.hologram.component.HologramOffset;
import su.nightexpress.excellentcrates.crates.hologram.editor.HologramEditorService;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramOffsetDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.dialog.context.CrateHologramTextDialogContext;
import su.nightexpress.excellentcrates.crates.hologram.editor.ui.menu.context.CrateHologramOptionsMenuContext;

@NullMarked
public class CrateHologramEditorUIController implements FeedbackHandler {

    private final HologramEditorService        editorService;
    private final CrateHologramEditorUIService uiService;
    private final MessageDispatcher            dispatcher;

    public CrateHologramEditorUIController(HologramEditorService editorService,
                                           CrateHologramEditorUIService uiService,
                                           MessageDispatcher dispatcher) {
        this.editorService = editorService;
        this.uiService = uiService;
        this.dispatcher = dispatcher;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    public void onExtensionClick(Player player, Identifier crateId, CrateEditorHook hook,
                                 BackwardNavigator backwardNavigator) {
        CrateHologramOptionsMenuContext context = new CrateHologramOptionsMenuContext(hook, crateId, backwardNavigator);

        this.handleFeedback(player, this.uiService.openHologramOptionsMenu(player, context));
    }

    public void onHologramOptionsStateClick(Player player, CrateHologramOptionsMenuContext context, boolean newState) {
        CrateEditorHook hook = context.hook();

        // Feedback is handled by the editor hook, so we don't need to handle it here.
        this.editorService.setHologramEnabled(hook, newState);
    }

    public void onHologramOptionsTextClick(Player player, CrateHologramOptionsMenuContext context,
                                           List<String> currentText,
                                           Runnable refreshUI) {
        Identifier crateId = context.crateId();
        CrateEditorHook hook = context.hook();
        CrateHologramTextDialogContext dialogContext = new CrateHologramTextDialogContext(hook, crateId, currentText);

        this.handleFeedback(player, this.uiService.showHologramTextDialog(player, dialogContext, refreshUI));
    }

    public void onHologramOptionsOffsetClick(Player player, CrateHologramOptionsMenuContext context,
                                             HologramOffset currentOffset,
                                             Runnable refreshUI) {
        Identifier crateId = context.crateId();
        CrateEditorHook hook = context.hook();

        double currentX = currentOffset.getX();
        double currentY = currentOffset.getY();
        double currentZ = currentOffset.getZ();

        CrateHologramOffsetDialogContext dialogContext = new CrateHologramOffsetDialogContext(hook, crateId, currentX,
            currentY,
            currentZ);

        this.handleFeedback(player, this.uiService.showHologramOffsetDialog(player, dialogContext, refreshUI));
    }

    public void onHologramTextDialogConfirm(Player player, CrateHologramTextDialogContext context,
                                            List<String> newText) {
        CrateEditorHook hook = context.hook();

        this.handleFeedback(player, this.editorService.setHologramText(hook, newText));
    }

    public void onHologramOffsetDialogConfirm(Player player, CrateHologramOffsetDialogContext context,
                                              HologramOffset newOffset) {
        CrateEditorHook hook = context.hook();

        this.handleFeedback(player, this.editorService.setHologramOffset(hook, newOffset));
    }
}
