package su.nightexpress.excellentcrates.api.reward.registry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardRegistry extends RewardResolver {

    @Override
    default @Nullable Reward resolveReward(RewardId id) {
        return this.get(id);
    }

    default Set<Reward> rewards() {
        return this.values();
    }

    default RewardReference createReference(Reward reward) {
        return this.createReference(reward.id());
    }

    default void register(Reward reward) {
        this.put(reward.id(), reward);
    }

    default void unregister(Reward reward) {
        this.remove(reward.id());
    }

    RewardReference createReference(RewardId id);

    int size();

    boolean isEmpty();

    boolean containsKey(RewardId key);

    boolean containsValue(Reward value);

    Reward get(RewardId key);

    Reward put(RewardId key, Reward value);

    Reward remove(RewardId key);

    void putAll(Map<? extends RewardId, ? extends Reward> m);

    void clear();

    Set<RewardId> keySet();

    Set<Reward> values();

    Set<Entry<RewardId, Reward>> entrySet();

    Reward getOrDefault(RewardId key, Reward defaultValue);

    void forEach(BiConsumer<? super RewardId, ? super Reward> action);

    Reward putIfAbsent(RewardId key, Reward value);
}
