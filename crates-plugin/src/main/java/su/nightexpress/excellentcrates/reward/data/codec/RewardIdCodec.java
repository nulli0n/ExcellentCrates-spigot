package su.nightexpress.excellentcrates.reward.data.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.core.codec.IdentifierCodec;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardIdCodec implements ConfigCodec<RewardId> {

    public static final RewardIdCodec INSTANCE = new RewardIdCodec();

    @Override
    public RewardId read(FileConfig config, String path) throws CodecReadException {
        Identifier crateId = config.getOrSet(path + ".namespace", IdentifierCodec.INSTANCE, new Identifier("none"));
        Identifier rewardId = config.getOrSet(path + ".value", IdentifierCodec.INSTANCE, new Identifier("none"));

        return new RewardId(crateId, rewardId);
    }

    @Override
    public void write(FileConfig config, String path, RewardId value) {
        config.set(path + ".namespace", value.crateId().toString());
        config.set(path + ".value", value.rewardId().toString());
    }
}
