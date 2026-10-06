package su.nightexpress.excellentcrates.preview.inventory.codec;

import org.bukkit.Material;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.preview.inventory.menu.InventoryButton;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.util.bukkit.NightItem;

@NullMarked
public class InventoryButtonCodec implements ConfigCodec<InventoryButton> {

    public static final InventoryButtonCodec INSTANCE = new InventoryButtonCodec();

    @Override
    public InventoryButton read(FileConfig config, String path) throws CodecReadException {
        NightItem icon = config.getOrSet(path + ".icon", ConfigCodecs.NIGHT_ITEM, NightItem.fromType(Material.STONE));
        int[] slots = config.getOrSet(path + ".slots", ConfigCodecs.INT_ARRAY, new int[0]);

        return new InventoryButton(icon, slots);
    }

    @Override
    public void write(FileConfig config, String path, InventoryButton value) {
        config.set(path + ".icon", value.getIcon());
        config.setArray(path + ".slots", value.getSlots());
    }
}
