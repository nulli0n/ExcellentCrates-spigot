package su.nightexpress.excellentcrates.crate.limit;

import org.jetbrains.annotations.NotNull;

public enum CooldownMode {

    DAILY("daily"),
    CUSTOM("custom");

    private final String id;

    CooldownMode(@NotNull String id) {
        this.id = id;
    }

    @NotNull
    public String id() {
        return this.id;
    }
}
