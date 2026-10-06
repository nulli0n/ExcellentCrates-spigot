package su.nightexpress.excellentcrates.api.crate.block.handler;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.action.ActionReason;

@NullMarked
public enum HandlerResult implements ActionReason {

    SUCCESS,
    DENY,
    IGNORE;
}
