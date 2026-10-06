package su.nightexpress.excellentcrates.reward.feature.limit.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.core.codec.IdentifierCodec;
import su.nightexpress.excellentcrates.core.common.limit.DefaultLimitOptions;
import su.nightexpress.excellentcrates.core.common.limit.codec.LimitOptionsCodec;
import su.nightexpress.excellentcrates.reward.feature.limit.component.DefaultRewardLimitComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardLimitComponentCodec implements ConfigCodec<DefaultRewardLimitComponent> {

    public static final RewardLimitComponentCodec INSTANCE = new RewardLimitComponentCodec();

    @Override
    public DefaultRewardLimitComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, false);

        LimitOptions globalOptions = config.getOrSet(path + ".global", LimitOptionsCodec.INSTANCE,
            DefaultLimitOptions.createDefault());

        LimitOptions individualOptions = config.getOrSet(path + ".individual", LimitOptionsCodec.INSTANCE,
            DefaultLimitOptions.createDefault());

        boolean alternativeEnabled = config.getOrSet(path + ".alternative.enabled", ConfigCodecs.BOOLEAN, false);
        Identifier alternativeRewardId = config.getOrSet(path + ".alternative.reward_id", IdentifierCodec.INSTANCE,
            new Identifier("none")
        );

        return new DefaultRewardLimitComponent.Builder()
            .setEnabled(enabled)
            .setGlobalOptions(globalOptions)
            .setIndividualOptions(individualOptions)
            .setAlternativeEnabled(alternativeEnabled)
            .setAlternativeRewardId(alternativeRewardId)
            .build();
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardLimitComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".global", value.getGlobalOptions());
        config.set(path + ".individual", value.getIndividualOptions());
        config.set(path + ".alternative.enabled", value.isAlternativeEnabled());
        config.set(path + ".alternative.reward_id", value.getAlternativeRewardId());
    }
}
