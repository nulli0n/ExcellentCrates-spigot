package su.nightexpress.excellentcrates.util;

public enum InteractType {

    CRATE_OPEN,
    CRATE_PREVIEW;

    public InteractType reversed() {
        return this == CRATE_OPEN ? CRATE_PREVIEW : CRATE_OPEN;
    }
}
