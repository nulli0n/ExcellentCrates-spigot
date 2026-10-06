package su.nightexpress.excellentcrates.api.crate.interact.action;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.excellentcrates.api.crate.interact.context.CrateInteractContext;

@NullMarked
public interface InteractAction extends Identifiable {

    void perform(CrateInteractContext context);
}
