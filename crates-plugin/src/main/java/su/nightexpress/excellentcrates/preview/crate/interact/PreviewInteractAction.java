package su.nightexpress.excellentcrates.preview.crate.interact;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.menu.BackwardNavigator;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;
import su.nightexpress.excellentcrates.api.preview.PreviewContext;
import su.nightexpress.excellentcrates.preview.DefaultPreviewContext;
import su.nightexpress.excellentcrates.preview.view.PreviewViewService;

@NullMarked
public class PreviewInteractAction implements InteractAction, FeedbackHandler {

    private static final Identifier ID = new Identifier("preview_crate");

    private final PreviewViewService viewService;
    private final MessageDispatcher  dispatcher;

    public PreviewInteractAction(PreviewViewService viewService, MessageDispatcher dispatcher) {
        this.viewService = viewService;
        this.dispatcher = dispatcher;
    }

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public MessageDispatcher getDispatcher() {
        return this.dispatcher;
    }

    @Override
    public void perform(CrateInteractContext context) {
        Player player = context.player();
        Crate crate = context.crate();

        BackwardNavigator navigator = Player::closeInventory;
        PreviewContext previewContext = new DefaultPreviewContext(crate, navigator);

        this.handleFeedback(player, this.viewService.openPreview(player, previewContext));
    }
}
