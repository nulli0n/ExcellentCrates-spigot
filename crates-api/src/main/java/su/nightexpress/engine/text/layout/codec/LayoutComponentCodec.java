package su.nightexpress.engine.text.layout.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.text.layout.LayoutComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class LayoutComponentCodec implements ConfigCodec<LayoutComponent> {

    public static final LayoutComponentCodec INSTANCE = new LayoutComponentCodec();

    @Override
    public LayoutComponent read(FileConfig config, String path) throws CodecReadException {
        String condition = config.getOrSet(path + ".condition", ConfigCodecs.STRING, "");
        String format = config.getOrSet(path + ".format", ConfigCodecs.STRING, "");
        String fallback = config.getOrSet(path + ".fallback", ConfigCodecs.STRING, "");
        return new LayoutComponent(condition, format, fallback);
    }

    @Override
    public void write(FileConfig config, String path, LayoutComponent value) {
        config.set(path + ".condition", value.condition());
        config.set(path + ".format", value.format());
        config.set(path + ".fallback", value.fallback());
    }
}
