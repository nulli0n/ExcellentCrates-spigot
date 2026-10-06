package su.nightexpress.excellentcrates.effect.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.effect.crate.EffectComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class DefaultEffectComponent implements EffectComponent {

    private boolean    enabled;
    private AdaptedKey profileKey;

    public DefaultEffectComponent(boolean enabled, AdaptedKey profileKey) {
        this.enabled = enabled;
        this.profileKey = profileKey;
    }

    public static DefaultEffectComponent createDefault() {
        return new DefaultEffectComponent(true, BukkitKeys.create("helix", "default"));
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public AdaptedKey getProfileKey() {
        return this.profileKey;
    }

    @Override
    public void setProfileKey(AdaptedKey profileKey) {
        this.profileKey = profileKey;
    }
}
