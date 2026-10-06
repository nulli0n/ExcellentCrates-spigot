package su.nightexpress.excellentcrates.reward.feature.cooldown;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.cooldown.IRewardCooldownService;
import su.nightexpress.excellentcrates.api.reward.cooldown.RewardCooldownsAPI;

@NullMarked
public class DefaultRewardCooldownsAPI implements RewardCooldownsAPI {

    private final IRewardCooldownService cooldownService;

    public DefaultRewardCooldownsAPI(IRewardCooldownService cooldownService) {
        this.cooldownService = cooldownService;
    }

    public IRewardCooldownService getCooldownService() {
        return cooldownService;
    }
}
