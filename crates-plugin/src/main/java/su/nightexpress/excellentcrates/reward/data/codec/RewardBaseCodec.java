package su.nightexpress.excellentcrates.reward.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardBaseCodec implements ConfigCodec<StandardRewardBase> {

    public static final RewardBaseCodec INSTANCE = new RewardBaseCodec();

    @Override
    public StandardRewardBase read(FileConfig config, String path) throws CodecReadException {
        double weight = config.getOrSet(path + ".weight", ConfigCodecs.DOUBLE, 0D);

        return new StandardRewardBase(weight);
    }

    @Override
    public void write(FileConfig config, String path, StandardRewardBase value) {
        config.set(path + ".weight", value.getWeight());
    }

}
