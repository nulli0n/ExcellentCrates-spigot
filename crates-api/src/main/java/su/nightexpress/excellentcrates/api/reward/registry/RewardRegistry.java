package su.nightexpress.excellentcrates.api.reward.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.nightcore.bridge.registry.NRegistry;

@NullMarked
public interface RewardRegistry extends NRegistry<Identifier, Reward>, RewardResolver {

    @Override
    default @Nullable Reward resolveReward(Identifier id) {
        return this.get(id);
    }

    default Set<Reward> rewards() {
        return this.values();
    }

    default RewardReference createReference(Reward reward) {
        return this.createReference(reward.id());
    }

    RewardReference createReference(Identifier id);

    boolean contains(Identifier key);

    void remove(Reward reward);
}
