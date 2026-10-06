package su.nightexpress.excellentcrates.reward.feature.cooldown.component.codec;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.cooldown.CooldownOptions;
import su.nightexpress.excellentcrates.reward.feature.cooldown.component.StandardRewardCooldownComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardCooldownsCodec implements ConfigCodec<StandardRewardCooldownComponent> {

    public static final RewardCooldownsCodec INSTANCE = new RewardCooldownsCodec();

    @Override
    public StandardRewardCooldownComponent read(FileConfig config, String path) throws CodecReadException {
        CooldownOptions globalCooldown = config.getOrSet(path + ".global", CooldownOptions.class,
            CooldownOptions.defaults());

        CooldownOptions individualCooldown = config.getOrSet(path + ".individual", CooldownOptions.class,
            CooldownOptions.defaults());

        return new StandardRewardCooldownComponent(globalCooldown, individualCooldown);
    }

    @Override
    public void write(FileConfig config, String path, StandardRewardCooldownComponent value) {
        config.set(path + ".global", value.getGlobalCooldown());
        config.set(path + ".individual", value.getIndividualCooldown());
    }
}
