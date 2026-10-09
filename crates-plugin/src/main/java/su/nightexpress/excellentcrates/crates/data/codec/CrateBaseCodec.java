package su.nightexpress.excellentcrates.crates.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CrateBaseCodec implements ConfigCodec<StandardCrateBase> {

    public static final CrateBaseCodec INSTANCE = new CrateBaseCodec();

    @Override
    public StandardCrateBase read(FileConfig config, String path) throws CodecReadException {
        boolean permissionRequired = config.getBoolean(path + ".permission_required", false);

        return new StandardCrateBase(permissionRequired);
    }

    @Override
    public void write(FileConfig config, String path, StandardCrateBase value) {
        config.set(path + ".permission_required", value.isPermissionRequired());
    }
}
