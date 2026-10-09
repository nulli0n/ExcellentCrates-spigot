package su.nightexpress.excellentcrates.reward.crate.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.reward.crate.component.model.StandardRewardEntry;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardEntryCodec implements ConfigCodec<StandardRewardEntry> {

    public static final RewardEntryCodec INSTANCE = new RewardEntryCodec();

    @Override
    public StandardRewardEntry read(FileConfig config, String path) throws CodecReadException {
        String rawRewardId = config.get(path + ".reward_id", ConfigCodecs.STRING);
        if (rawRewardId == null) throw new CodecReadException("Missing 'reward_id' property");

        Identifier rewardId = IdentifierParser.parse(rawRewardId)
            .orElseThrow(() -> new CodecReadException("Invalid reward ID: " + rawRewardId));

        return new StandardRewardEntry(rewardId);
    }

    @Override
    public void write(FileConfig config, String path, StandardRewardEntry value) {
        config.set(path + ".reward_id", value.getRewardId().value());
    }
}
