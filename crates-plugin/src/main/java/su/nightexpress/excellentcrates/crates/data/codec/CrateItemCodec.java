package su.nightexpress.excellentcrates.crates.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.crates.data.crate.CrateItem;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.codec.AdaptedItemCodec;

@NullMarked
public class CrateItemCodec implements ConfigCodec<CrateItem> {

    public static final CrateItemCodec INSTANCE = new CrateItemCodec();

    @Override
    public CrateItem read(FileConfig config, String path) throws CodecReadException {
        AdaptedItem item = AdaptedItemCodec.read(config, path + ".item");
        boolean itemStackable = config.getBoolean(path + ".item_stackable", true);
        boolean useDisplay = config.getOrSet(path + ".use_display", ConfigCodecs.BOOLEAN, true);

        return new CrateItem(item, itemStackable, useDisplay);
    }

    @Override
    public void write(FileConfig config, String path, CrateItem value) {
        if (!ItemHelper.isBroken(value.getItem())) {
            config.set(path + ".item", value.getItem());
        }
        config.set(path + ".item_stackable", value.isStackable());
        config.set(path + ".use_display", value.isUseDisplay());
    }
}
