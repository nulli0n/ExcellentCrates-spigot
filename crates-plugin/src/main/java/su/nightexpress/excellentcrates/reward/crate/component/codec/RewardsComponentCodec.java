package su.nightexpress.excellentcrates.reward.crate.component.codec;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardEntry;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardsComponent;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;
import su.nightexpress.nightcore.configuration.exception.CodecReadException;

@NullMarked
public class RewardsComponentCodec implements ConfigCodec<DefaultRewardsComponent> {

    public static final RewardsComponentCodec INSTANCE = new RewardsComponentCodec();

    @Override
    public DefaultRewardsComponent read(FileConfig config, String path) throws CodecReadException {
        Map<Identifier, CrateRewardEntry> rewardMap = new HashMap<>();

        config.getSection(path + ".reward_list").forEach(sId -> {
            DefaultRewardEntry crateReward = config.get(path + ".reward_list." + sId, DefaultRewardEntry.class);
            if (crateReward == null) return;

            rewardMap.put(crateReward.getRewardId(), crateReward);
        });

        int requiredRewards = config.getOrSet(path + ".required_rewards", ConfigCodecs.INT, 1);

        return new DefaultRewardsComponent(rewardMap, requiredRewards);
    }

    @Override
    public void write(FileConfig config, String path, DefaultRewardsComponent value) {
        config.remove(path + ".reward_list");

        value.getRewardByIdMap().forEach((id, reward) -> {
            config.set(path + ".reward_list." + UUID.randomUUID(), reward);
        });

        config.set(path + ".required_rewards", value.getRequiredAmount());
    }
}
