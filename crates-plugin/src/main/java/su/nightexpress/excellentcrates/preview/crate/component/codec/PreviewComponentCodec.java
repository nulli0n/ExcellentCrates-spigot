package su.nightexpress.excellentcrates.preview.crate.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.preview.crate.component.DefaultPreviewComponent;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class PreviewComponentCodec implements ConfigCodec<DefaultPreviewComponent> {

    public static final PreviewComponentCodec INSTANCE = new PreviewComponentCodec();

    @Override
    public DefaultPreviewComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);
        String rawPreviewKey = config.getOrSet(path + ".config", ConfigCodecs.STRING,
            DefaultPreviewComponent.DEFAULT_PREVIEW_KEY.asString()
        );

        AdaptedKey previewKey = BukkitKeys.parse(rawPreviewKey).orElse(null);
        if (previewKey == null) {
            throw new CodecReadException("Invalid preview key: " + rawPreviewKey);
        }

        return new DefaultPreviewComponent(enabled, previewKey);
    }

    @Override
    public void write(FileConfig config, String path, DefaultPreviewComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".config", value.getPreviewKey().asString());
    }
}
