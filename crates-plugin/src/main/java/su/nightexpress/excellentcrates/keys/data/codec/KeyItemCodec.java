package su.nightexpress.excellentcrates.keys.data.codec;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.keys.data.key.StandardKeyItem;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;

@NullMarked
public class KeyItemCodec implements ConfigCodec<StandardKeyItem> {

    public static final KeyItemCodec INSTANCE = new KeyItemCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyItemCodec.class);

    @Override
    public StandardKeyItem read(FileConfig config, String path) throws CodecReadException {
        AdaptedItem item = AdaptedItemStack.read(config, path + ".item");
        if (item == null) {
            LOGGER.warn("Failed to read key item at path '{}', using default item instead.", path);
            item = ItemHelper.vanilla(new ItemStack(Material.TRIPWIRE_HOOK));
        }

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
