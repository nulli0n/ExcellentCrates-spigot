package su.nightexpress.excellentcrates.api.reward.grant;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardGrantAPI {

    void registerProcessor(int priority, RewardGrantProcessor processor);

    TinyRegistry<RegisteredGrantProcessor> getProcessors();

    void giveReward(Player player, Crate crate, Reward reward);
}
