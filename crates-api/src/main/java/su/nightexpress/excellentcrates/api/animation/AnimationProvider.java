package su.nightexpress.excellentcrates.api.animation;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface AnimationProvider<C extends AnimationProfile> extends Identifiable {

    @NonNull
    C readProfile(AdaptedKey key, FileConfig config);

    void writeDefaultProfile(AdaptedKey key, FileConfig config);
}
