package su.nightexpress.excellentcrates.reward.data.codec;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardPreview;
import su.nightexpress.excellentcrates.util.ItemHelper;
import su.nightexpress.nightcore.bridge.item.AdaptedItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;
import su.nightexpress.nightcore.integration.item.codec.AdaptedItemCodec;

@NullMarked
public class RewardPreviewCodec implements ConfigCodec<StandardRewardPreview> {

    public static final RewardPreviewCodec INSTANCE = new RewardPreviewCodec();

    @Override
    public StandardRewardPreview read(FileConfig config, String path) throws CodecReadException {
        String name = config.getOrSet(path + ".name", ConfigCodecs.STRING, "No Name");
        List<String> lore = config.getOrSet(path + ".lore", ConfigCodecs.STRING_LIST, List.of());
        AdaptedItem preview = AdaptedItemCodec.read(config, path + ".icon");
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
