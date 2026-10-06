package su.nightexpress.excellentcrates.animation.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.animation.component.AnimationComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class DefaultAnimationComponent implements AnimationComponent {

    public static final AdaptedKey DEFAULT_KEY = BukkitKeys.create("none", "null");

    private boolean    enabled;
    private AdaptedKey profileKey;

    public DefaultAnimationComponent(boolean enabled, AdaptedKey profileKey) {
        this.enabled = enabled;
        this.profileKey = profileKey;
    }

    public static DefaultAnimationComponent createDefault() {
        return new DefaultAnimationComponent(false, DEFAULT_KEY);
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public AdaptedKey getProfileKey() {
        return profileKey;
    }

    @Override
    public void setProfileKey(AdaptedKey profileKey) {
        this.profileKey = profileKey;
    }
}
