package su.nightexpress.excellentcrates.reward.crate.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardEntry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardEntryCodec implements ConfigCodec<DefaultRewardEntry> {

    public static final RewardEntryCodec INSTANCE = new RewardEntryCodec();

    @Override
    public DefaultRewardEntry read(FileConfig config, String path) throws CodecReadException {
        String rawRewardId = config.get(path + ".reward_id", ConfigCodecs.STRING);
        if (rawRewardId == null) throw new CodecReadException("Missing 'reward_id' property");

        Identifier rewardId = IdentifierParser.parse(rawRewardId)
            .orElseThrow(() -> new CodecReadException("Invalid reward ID: " + rawRewardId));

        double weight = config.getOrSet(path + ".weight", ConfigCodecs.DOUBLE, 0D);

        return new DefaultRewardEntry(rewardId, weight);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardEntry value) {
        config.set(path + ".reward_id", value.getRewardId().value());
        config.set(path + ".weight", value.getWeight());
    }
}
