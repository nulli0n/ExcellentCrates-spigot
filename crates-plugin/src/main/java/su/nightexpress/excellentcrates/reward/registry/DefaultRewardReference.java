package su.nightexpress.excellentcrates.reward.registry;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;

@NullMarked
public class DefaultRewardReference implements RewardReference {

    private final Identifier     id;
    private final RewardRegistry registry;

    public DefaultRewardReference(Identifier id, RewardRegistry registry) {
        this.id = id;
        this.registry = registry;
    }

    @Override
    public @Nullable Reward get() {
        return this.registry.get(this.id);
    }

    @Override
    public Identifier id() {
        return this.id;
    }
}
