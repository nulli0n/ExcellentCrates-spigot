package su.nightexpress.excellentcrates.api.common.cooldown;

import org.jspecify.annotations.NullMarked;

@NullMarked
public enum CooldownMode {

    DAILY("daily"),
    CUSTOM("custom");

    private final String id;

    CooldownMode(String id) {
        this.id = id;
    }

    public String id() {
        return this.id;
    }
}
