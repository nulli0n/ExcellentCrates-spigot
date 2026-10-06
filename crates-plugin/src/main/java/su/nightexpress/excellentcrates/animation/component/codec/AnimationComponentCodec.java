package su.nightexpress.excellentcrates.animation.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.excellentcrates.animation.component.DefaultAnimationComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class AnimationComponentCodec implements ConfigCodec<DefaultAnimationComponent> {

    public static final AnimationComponentCodec INSTANCE = new AnimationComponentCodec();

    @Override
    public DefaultAnimationComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);
        String profileName = config.getOrSet(path + ".profile", ConfigCodecs.STRING,
            DefaultAnimationComponent.DEFAULT_KEY.asString()
        );

        AdaptedKey profileKey = BukkitKeys.parse(profileName).orElse(null);
        if (profileKey == null) {
            throw new CodecReadException("Invalid animation profile key: " + profileName);
        }

        return new DefaultAnimationComponent(enabled, profileKey);
    }

    @Override
    public void write(FileConfig config, String path, DefaultAnimationComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".profile", value.getProfileKey().asString());
    }
}
