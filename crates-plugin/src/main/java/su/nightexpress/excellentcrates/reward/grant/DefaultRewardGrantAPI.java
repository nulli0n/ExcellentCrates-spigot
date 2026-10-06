package su.nightexpress.excellentcrates.reward.grant;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.grant.RegisteredGrantProcessor;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantAPI;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;

@NullMarked
public class DefaultRewardGrantAPI implements RewardGrantAPI {

    private final TinyRegistry<RegisteredGrantProcessor> processors;
    private final RewardGrantService                     grantService;

    public DefaultRewardGrantAPI(TinyRegistry<RegisteredGrantProcessor> processors, RewardGrantService grantService) {
        this.processors = processors;
        this.grantService = grantService;
    }

    @Override
    public TinyRegistry<RegisteredGrantProcessor> getProcessors() {
        return this.processors;
    }

    @Override
    public void giveReward(Player player, Crate crate, Reward reward) {
        this.grantService.giveReward(player, crate, reward);
    }

    @Override
    public void registerProcessor(int priority, RewardGrantProcessor processor) {
        this.grantService.registerProcessor(priority, processor);
    }
}
