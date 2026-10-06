package su.nightexpress.excellentcrates.api.reward.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardResolver {

    @Nullable
    Reward resolveReward(Identifier id);

    Set<Reward> rewards();
}
