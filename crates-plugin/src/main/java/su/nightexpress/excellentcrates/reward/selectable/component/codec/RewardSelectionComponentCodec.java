package su.nightexpress.excellentcrates.reward.selectable.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.selectable.component.data.DefaultSelectiveRewardsComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardSelectionComponentCodec implements ConfigCodec<DefaultSelectiveRewardsComponent> {

    public static final RewardSelectionComponentCodec INSTANCE = new RewardSelectionComponentCodec();

    @Override
    public DefaultSelectiveRewardsComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);

        return new DefaultSelectiveRewardsComponent(enabled);
    }

    @Override
    public void write(FileConfig config, String path, DefaultSelectiveRewardsComponent value) {
        config.set(path + ".enabled", ConfigCodecs.BOOLEAN, value.isEnabled());
    }
}
