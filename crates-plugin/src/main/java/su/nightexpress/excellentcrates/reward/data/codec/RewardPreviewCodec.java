package su.nightexpress.excellentcrates.reward.data.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardPreview;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.impl.AdaptedItemStack;

@NullMarked
public class RewardPreviewCodec implements ConfigCodec<StandardRewardPreview> {

    public static final RewardPreviewCodec INSTANCE = new RewardPreviewCodec();

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPreviewCodec.class);

    @Override
    public StandardRewardPreview read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "No Name");
        List<String> lore = config.getOrSet(path + ".lore", ConfigCodecs.STRING_LIST, List.of());
        AdaptedItem preview = AdaptedItemStack.read(config, path + ".icon");
        if (preview == null) {
            preview = ItemHelper.adaptedPlaceholder();
            LOGGER.warn("Failed to read preview icon for reward at path '{}', using placeholder instead.", path);
        }
        boolean useItemData = config.getOrSet(path + ".use_item_data", ConfigCodecs.BOOLEAN, false);

        return new StandardRewardPreview(name, lore, preview, useItemData);
    }

    @Override
    public void write(FileConfig config, String path, StandardRewardPreview value) {
        config.set(path + ".name", value.getName());
        config.set(path + ".lore", value.getLore());

        if (!ItemHelper.isBroken(value.getIcon())) {
            config.set(path + ".icon", value.getIcon());
        }

        config.set(path + ".use_item_data", value.isUseIconData());
    }
}
