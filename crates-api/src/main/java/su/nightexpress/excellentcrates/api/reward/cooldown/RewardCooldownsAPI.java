package su.nightexpress.excellentcrates.api.reward.cooldown;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RewardCooldownsAPI {

    IRewardCooldownService getCooldownService();
}
