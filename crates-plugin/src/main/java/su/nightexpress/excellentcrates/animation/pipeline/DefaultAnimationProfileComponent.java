package su.nightexpress.excellentcrates.animation.pipeline;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.pipeline.component.AnimationProfileComponent;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;

@NullMarked
public class DefaultAnimationProfileComponent implements AnimationProfileComponent {

    private AdaptedKey profileKey;

    public DefaultAnimationProfileComponent(AdaptedKey profileKey) {
        this.profileKey = profileKey;
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
