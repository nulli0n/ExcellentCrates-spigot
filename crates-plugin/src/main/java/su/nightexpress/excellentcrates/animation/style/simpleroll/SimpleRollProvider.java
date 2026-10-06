package su.nightexpress.excellentcrates.animation.style.simpleroll;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.animation.AnimationProvider;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SimpleRollProvider implements AnimationProvider<SimpleRollProfile> {

    private static final Identifier ID = new Identifier("simple_roll");

    @Override
    public Identifier getId() {
        return ID;
    }

    @Override
    public SimpleRollProfile readProfile(AdaptedKey key, FileConfig config) {
        SimpleRollSettings settings = config.getOrSet("settings",
            SimpleRollSettings.class,
            SimpleRollSettings.defaultSettings()
        );

        return new SimpleRollProfile(key, settings);
    }

    @Override
    public void writeDefaultProfile(AdaptedKey key, FileConfig config) {
        config.set("settings", SimpleRollSettings.defaultSettings());
    }
}
