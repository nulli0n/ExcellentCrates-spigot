package su.nightexpress.excellentcrates.reward.registry;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;

@NullMarked
public class DefaultRewardRegistry implements RewardRegistry {

    private final Map<RewardId, Reward> rewards;

    public DefaultRewardRegistry() {
        this.rewards = new ConcurrentHashMap<>();
    }

    @Override
    public RewardReference createReference(RewardId id) {
        return new DefaultRewardReference(id, this);
    }

    public int size() {
        return rewards.size();
    }

    public boolean isEmpty() {
        return rewards.isEmpty();
    }

    public boolean containsKey(RewardId key) {
        return rewards.containsKey(key);
    }

    public boolean containsValue(Reward value) {
        return rewards.containsValue(value);
    }

    public Reward get(RewardId key) {
        return rewards.get(key);
    }

    public Reward put(RewardId key, Reward value) {
        return rewards.put(key, value);
    }

    public Reward remove(RewardId key) {
        return rewards.remove(key);
    }

    public void putAll(Map<? extends RewardId, ? extends Reward> m) {
        rewards.putAll(m);
    }

    public void clear() {
        rewards.clear();
    }

    public Set<RewardId> keySet() {
        return Set.copyOf(rewards.keySet());
    }

    public Set<Reward> values() {
        return Set.copyOf(rewards.values());
    }

    public Set<Entry<RewardId, Reward>> entrySet() {
        return Set.copyOf(rewards.entrySet());
    }

    public Reward getOrDefault(RewardId key, Reward defaultValue) {
        return rewards.getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super RewardId, ? super Reward> action) {
        rewards.forEach(action);
    }

    public Reward putIfAbsent(RewardId key, Reward value) {
        return rewards.putIfAbsent(key, value);
    }
}
