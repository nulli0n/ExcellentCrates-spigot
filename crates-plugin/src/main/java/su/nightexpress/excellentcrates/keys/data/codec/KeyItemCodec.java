package su.nightexpress.excellentcrates.keys.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.keys.data.key.StandardKeyItem;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.codec.AdaptedItemCodec;

@NullMarked
public class KeyItemCodec implements ConfigCodec<StandardKeyItem> {

    public static final KeyItemCodec INSTANCE = new KeyItemCodec();

    @Override
    public StandardKeyItem read(FileConfig config, String path) throws CodecReadException {
        AdaptedItem item = AdaptedItemCodec.read(config, path + ".item");
        boolean stackable = config.getOrSet(path + ".stackable", ConfigCodecs.BOOLEAN, true);
        boolean inheritDisplaySettings = config.getOrSet(path + ".inherit_display_settings", ConfigCodecs.BOOLEAN,
            true);

        return new StandardKeyItem(item, stackable, inheritDisplaySettings);
    }

    @Override
    public void write(FileConfig config, String path, StandardKeyItem value) {
        config.set(path + ".item", value.getItem());
        config.set(path + ".stackable", value.isStackable());
        config.set(path + ".inherit_display_settings", value.isInheritDisplaySettings());
    }
}
