package su.nightexpress.excellentcrates.rarity;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;

@NullMarked
public class RarityRepository implements RarityRegistry {

    private final IdentifiableRegistry<Rarity> registry;

    public RarityRepository() {
        this.registry = new IdentifiableRegistry<>();
    }

    public void clear() {
        registry.clear();
    }

    public int size() {
        return registry.size();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public Set<Identifier> keys() {
        return registry.ids();
    }

    public void register(Rarity item) {
        registry.register(item);
    }

    public @Nullable Rarity remove(Identifier id) {
        return registry.remove(id);
    }

    public @Nullable Rarity get(Identifier key) {
        return registry.get(key);
    }

    public Optional<Rarity> lookup(Identifier key) {
        return registry.lookup(key);
    }

    public Set<Rarity> values() {
        return registry.values();
    }

    public Set<Identifier> ids() {
        return registry.ids();
    }

    public Set<String> idValues() {
        return registry.idValues();
    }

    public Stream<Rarity> stream() {
        return registry.stream();
    }

    @Override
    public Optional<Rarity> findMostWeighted() {
        return this.stream()
            .max(Comparator.comparingDouble(Rarity::getWeight));
    }

    @Override
    public Optional<Rarity> findLessWeighted() {
        return this.stream()
            .min(Comparator.comparingDouble(Rarity::getWeight));
    }
}
