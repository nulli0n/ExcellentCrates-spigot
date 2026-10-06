package su.nightexpress.excellentcrates.reward.feature.commands.component.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;
import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandExecutionMode;
import su.nightexpress.excellentcrates.reward.feature.commands.component.StandrdRewardCommandPool;
import su.nightexpress.excellentcrates.reward.feature.commands.component.data.DefaultRewardCommandsComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardCommandContentCodec implements ConfigCodec<DefaultRewardCommandsComponent> {

    public static final RewardCommandContentCodec INSTANCE = new RewardCommandContentCodec();

    @Override
    public DefaultRewardCommandsComponent read(FileConfig config, String path) throws CodecReadException {
        boolean enabled = config.getOrSet(path + ".enabled", ConfigCodecs.BOOLEAN, true);
        int iterations = config.getOrSet(path + ".iterations", ConfigCodecs.INT, 1);

        RewardCommandExecutionMode giveMode = config.getOrSet(path + ".give_mode",
            ConfigCodecs.forEnum(RewardCommandExecutionMode.class),
            RewardCommandExecutionMode.NORMAL
        );

        Map<UUID, RewardCommandPool> bundles = new HashMap<>();

        config.getSection(path + ".bundles").forEach(sId -> {
            StandrdRewardCommandPool bundle = config.getOrSet(path + ".bundles." + sId,
                RewardCommandBundleCodec.INSTANCE,
                StandrdRewardCommandPool.createDefault()
            );

            bundles.put(bundle.getId(), bundle);
        });

        return new DefaultRewardCommandsComponent(enabled, iterations, giveMode, bundles);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardCommandsComponent value) {
        config.set(path + ".enabled", value.isEnabled());
        config.set(path + ".give_mode", value.getGiveMode());
        config.set(path + ".iterations", value.getIterations());

        config.set(path + ".bundles", null);

        value.getBundleMap().forEach((id, bundle) -> {
            config.set(path + ".bundles." + id, bundle);
        });
    }
}
