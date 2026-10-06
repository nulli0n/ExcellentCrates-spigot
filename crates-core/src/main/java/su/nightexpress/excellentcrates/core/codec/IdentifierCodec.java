package su.nightexpress.excellentcrates.core.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class IdentifierCodec implements ConfigCodec<Identifier> {

    public static final IdentifierCodec INSTANCE = new IdentifierCodec();

    @Override
    public Identifier read(FileConfig config, String path) throws CodecReadException {
        String raw = config.get(path, ConfigCodecs.STRING);
        if (raw == null) {
            throw new CodecReadException("Missing value for Identifier at path: " + path);
        }
        try {
            return new Identifier(raw);
        }
        catch (IllegalArgumentException e) {
            throw new CodecReadException("Invalid Identifier format at path: " + path, e);
        }
    }

    @Override
    public void write(FileConfig config, String path, Identifier value) {
        config.set(path, value.value());
    }
}
