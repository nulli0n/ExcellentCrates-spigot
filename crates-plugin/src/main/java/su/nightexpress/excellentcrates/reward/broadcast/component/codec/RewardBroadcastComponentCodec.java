package su.nightexpress.excellentcrates.reward.broadcast.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.broadcast.component.DefaultRewardBroadcastComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardBroadcastComponentCodec implements ConfigCodec<DefaultRewardBroadcastComponent> {

    public static final RewardBroadcastComponentCodec INSTANCE = new RewardBroadcastComponentCodec();

    @Override
    public DefaultRewardBroadcastComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);

        return new DefaultRewardBroadcastComponent(enabled);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardBroadcastComponent value) {
        config.set(path + ".enabled", ConfigCodecs.BOOLEAN, value.isEnabled());
    }
}
