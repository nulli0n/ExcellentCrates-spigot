package su.nightexpress.excellentcrates.api.common.cooldown;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record CooldownSnapshot(boolean enabled, CooldownMode mode, long duration) {

    public static CooldownSnapshot of(CooldownOptions cooldown) {
        return new CooldownSnapshot(cooldown.isEnabled(), cooldown.getMode(), cooldown.getDuration());
    }
}
