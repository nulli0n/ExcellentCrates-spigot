package su.nightexpress.excellentcrates.api.effect.crate;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public interface EffectComponent extends CrateComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    AdaptedKey getProfileKey();

    void setProfileKey(AdaptedKey profileKey);
}
