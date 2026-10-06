package su.nightexpress.excellentcrates.crates.cooldown.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.crates.cooldown.component.StandardCrateCooldownComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class CrateCooldownsCodec implements ConfigCodec<StandardCrateCooldownComponent> {

    public static final CrateCooldownsCodec INSTANCE = new CrateCooldownsCodec();

    @Override
    public StandardCrateCooldownComponent read(FileConfig config, String path) throws CodecReadException {
        CooldownOptions globalCooldown = config.getOrSet(path + ".global", CooldownOptions.class,
            CooldownOptions.defaults());

        CooldownOptions individualCooldown = config.getOrSet(path + ".individual", CooldownOptions.class,
            CooldownOptions.defaults());

        return new StandardCrateCooldownComponent(globalCooldown, individualCooldown);
    }

    @Override
    public void write(FileConfig config, String path, StandardCrateCooldownComponent value) {
        config.set(path + ".global", value.getGlobalCooldown());
        config.set(path + ".individual", value.getIndividualCooldown());
    }
}
