package su.nightexpress.excellentcrates.api.crate.interact;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;

@NullMarked
public interface InteractionAPI {

    ActionResult performAction(CrateInteractContext context, Identifier actionId);

    void registerAction(InteractAction action);
}
