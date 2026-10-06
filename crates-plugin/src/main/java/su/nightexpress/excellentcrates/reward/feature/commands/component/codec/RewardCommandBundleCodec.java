package su.nightexpress.excellentcrates.reward.feature.commands.component.codec;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.reward.feature.commands.component.StandrdRewardCommandPool;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardCommandBundleCodec implements ConfigCodec<StandrdRewardCommandPool> {

    public static final RewardCommandBundleCodec INSTANCE = new RewardCommandBundleCodec();

    @Override
    public StandrdRewardCommandPool read(FileConfig config, String path) throws CodecReadException {
        List<String> commands = config.getOrSet(path + ".commands", ConfigCodecs.STRING_LIST, List.of());
        double weight = config.getOrSet(path + ".weight", ConfigCodecs.DOUBLE, 0.0);

        return new StandrdRewardCommandPool(UUID.randomUUID(), commands, weight);
    }

    @Override
    public void write(FileConfig config, String path, StandrdRewardCommandPool value) {
        config.set(path + ".commands", value.getCommands());
        config.set(path + ".weight", value.getWeight());
    }
}
