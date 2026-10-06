package su.nightexpress.excellentcrates.reward.items.component.codec;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.reward.items.component.DefaultRewardItemsComponent;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;

@NullMarked
public class RewardItemsComponentCodec implements ConfigCodec<DefaultRewardItemsComponent> {

    public static final RewardItemsComponentCodec INSTANCE = new RewardItemsComponentCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardItemsComponentCodec.class);

    @Override
    public DefaultRewardItemsComponent read(FileConfig config, String path) throws CodecReadException {
        List<AdaptedItem> items = new ArrayList<>();

        config.getSection(path + ".item_list").forEach(sId -> {
            AdaptedItem item = AdaptedItemStack.read(config, path + ".item_list." + sId);
            if (item == null) {
                LOGGER.error("Invalid item data at path: '" + path + ".item_list." + sId + "'. Skipping this item.");
                return;
            }
            items.add(item);
        });

        return new DefaultRewardItemsComponent(items);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardItemsComponent value) {
        config.remove(path + ".item_list");

        for (AdaptedItem provider : value.getItems()) {
            config.set(path + ".item_list." + UUID.randomUUID(), provider);
        }
    }

}
