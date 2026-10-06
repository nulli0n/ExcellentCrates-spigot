package su.nightexpress.excellentcrates.crates.interact;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.interact.InteractionAPI;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;

@NullMarked
public class DefaultInteractionAPI implements InteractionAPI {

    private final IdentifiableRegistry<InteractAction> actions;

    private final CrateInteractionService interactionService;

    public DefaultInteractionAPI(IdentifiableRegistry<InteractAction> actions,
                                 CrateInteractionService interactionService) {
        this.actions = actions;
        this.interactionService = interactionService;
    }

    public ActionResult performAction(CrateInteractContext context, Identifier actionId) {
        return this.interactionService.performAction(context, actionId);
    }

    public void registerAction(InteractAction action) {
        this.actions.register(action);
    }
}
