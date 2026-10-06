package su.nightexpress.excellentcrates.effect.crate.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.effect.crate.component.DefaultEffectComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class EffectComponentCodec implements ConfigCodec<DefaultEffectComponent> {

    public static final EffectComponentCodec INSTANCE = new EffectComponentCodec();

    @Override
    public DefaultEffectComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);
        String rawProfileKey = config.getOrSet(path + ".profile", ConfigCodecs.STRING, "null");

        AdaptedKey profileKey = BukkitKeys.parse(rawProfileKey)
            .orElseThrow(() -> new CodecReadException("Invalid profile key: " + rawProfileKey));

        return new DefaultEffectComponent(enabled, profileKey);
    }

    @Override
    public void write(FileConfig config, String path, DefaultEffectComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".profile", value.getProfileKey().asString());
    }
}
