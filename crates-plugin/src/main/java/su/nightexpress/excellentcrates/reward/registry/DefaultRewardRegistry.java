package su.nightexpress.excellentcrates.reward.registry;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;

@NullMarked
public class DefaultRewardRegistry implements RewardRegistry {

    private final IdentifiableRegistry<Reward> registry;

    public DefaultRewardRegistry() {
        this.registry = new IdentifiableRegistry<>();
    }

    @Override
    public RewardReference createReference(Identifier id) {
        return new DefaultRewardReference(id, this);
    }

    @Override
    public boolean contains(Identifier key) {
        return this.get(key) != null;
    }

    @Override
    public void remove(Reward reward) {
        this.remove(reward.id());
    }

    @Override
    public void clear() {
        this.registry.clear();
    }

    @Override
    public @Nullable Reward get(Identifier key) {
        return this.registry.get(key);
    }

    @Override
    public Set<Identifier> keys() {
        return this.registry.ids();
    }

    @Override
    public Optional<Reward> lookup(Identifier key) {
        return this.registry.lookup(key);
    }

    @Override
    public void register(Reward item) {
        this.registry.register(item);
    }

    @Override
    public @Nullable Reward remove(Identifier id) {
        return this.registry.remove(id);
    }

    @Override
    public int size() {
        return this.registry.size();
    }

    @Override
    public Stream<Reward> stream() {
        return this.registry.stream();
    }

    @Override
    public Set<Reward> values() {
        return this.registry.values();
    }
}
