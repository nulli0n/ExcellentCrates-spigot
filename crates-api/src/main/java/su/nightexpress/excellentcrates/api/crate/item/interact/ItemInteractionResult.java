package su.nightexpress.excellentcrates.api.crate.item.interact;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionReason;

@NullMarked
public enum ItemInteractionResult implements ActionReason {

    SUCCESS,
    DENY,
    IGNORE;
}
