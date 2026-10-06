package su.nightexpress.excellentcrates.keys;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;

@NullMarked
public class KeyRepository implements KeyRegistry {

    private final IdentifiableRegistry<CrateKey> keyRegistry;

    public KeyRepository() {
        this.keyRegistry = new IdentifiableRegistry<>();
    }

    @Override
    public boolean contains(Identifier key) {
        return this.keyRegistry.get(key) != null;
    }

    @Override
    public void reindex(CrateKey key) {
        this.keyRegistry.remove(key.id());
        this.keyRegistry.register(key);
    }

    @Override
    public void remove(CrateKey key) {
        this.keyRegistry.remove(key.id());
    }

    @Override
    public void clear() {
        this.keyRegistry.clear();
    }

    @Override
    public @Nullable CrateKey get(Identifier key) {
        return this.keyRegistry.get(key);
    }

    @Override
    public Set<Identifier> keys() {
        return this.keyRegistry.ids();
    }

    @Override
    public Optional<CrateKey> lookup(Identifier key) {
        return this.keyRegistry.lookup(key);
    }

    @Override
    public void register(CrateKey item) {
        this.keyRegistry.register(item);
    }

    @Override
    public @Nullable CrateKey remove(Identifier id) {
        return this.keyRegistry.remove(id);
    }

    @Override
    public int size() {
        return this.keyRegistry.size();
    }

    @Override
    public Stream<CrateKey> stream() {
        return this.keyRegistry.stream();
    }

    @Override
    public Set<CrateKey> values() {
        return this.keyRegistry.values();
    }
}
