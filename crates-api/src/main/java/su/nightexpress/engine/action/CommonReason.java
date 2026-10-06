package su.nightexpress.engine.action;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum CommonReason implements ActionReason {
    SUCCESS,
    FAILURE,
    NO_PERMISSION,
    ;
}