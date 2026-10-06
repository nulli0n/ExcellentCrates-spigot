package su.nightexpress.excellentcrates.crates.data.codec;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.crates.data.crate.CrateItem;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;

@NullMarked
public class CrateItemCodec implements ConfigCodec<CrateItem> {

    public static final CrateItemCodec INSTANCE = new CrateItemCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateItemCodec.class);

    @Override
    public CrateItem read(FileConfig config, String path) throws CodecReadException {
        AdaptedItem item = AdaptedItemStack.read(config, path + ".item");
        if (item == null) {
            item = ItemHelper.adaptedPlaceholder();
            LOGGER.warn("Item data at path '{}' in '{}' is invalid, using placeholder.",
                path + ".item", config.getPath()
            );
        }

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
