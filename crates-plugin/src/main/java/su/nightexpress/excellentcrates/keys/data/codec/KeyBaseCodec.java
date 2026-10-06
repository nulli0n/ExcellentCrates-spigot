package su.nightexpress.excellentcrates.keys.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.data.key.StandardKeyBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class KeyBaseCodec implements ConfigCodec<StandardKeyBase> {

    public static final KeyBaseCodec INSTANCE = new KeyBaseCodec();

    @Override
    public StandardKeyBase read(FileConfig config, String path) throws CodecReadException {
        boolean virtual = config.getOrSet(path + ".virtual", ConfigCodecs.BOOLEAN, false);
        return new StandardKeyBase(virtual);
    }

    @Override
    public void write(FileConfig config, String path, StandardKeyBase value) {
        config.set(path + ".virtual", value.isVirtual());
    }

}
