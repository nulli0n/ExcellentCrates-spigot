package su.nightexpress.engine.text.layout.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.text.layout.LayoutComponentGroup;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class LayoutComponentGroupCodec implements ConfigCodec<LayoutComponentGroup> {

    public static final LayoutComponentGroupCodec INSTANCE = new LayoutComponentGroupCodec();

    @Override
    public LayoutComponentGroup read(FileConfig config, String path) throws CodecReadException {
        List<String> componentNames = config.getOrSet(path + ".components", ConfigCodecs.STRING_LIST, List.of());
        String delimiter = config.getOrSet(path + ".delimiter", ConfigCodecs.STRING, ",");
        String format = config.getOrSet(path + ".format", ConfigCodecs.STRING, "");

        return new LayoutComponentGroup(componentNames, delimiter, format);
    }

    @Override
    public void write(FileConfig config, String path, LayoutComponentGroup value) {
        config.set(path + ".components", value.componentNames());
        config.set(path + ".delimiter", value.delimiter());
        config.set(path + ".format", value.format());
    }
}
