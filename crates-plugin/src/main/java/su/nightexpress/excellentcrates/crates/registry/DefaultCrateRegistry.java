package su.nightexpress.excellentcrates.crates.registry;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public class DefaultCrateRegistry implements CrateRegistry {

    private final IdentifiableRegistry<Crate> registry;

    public DefaultCrateRegistry() {
        this.registry = new IdentifiableRegistry<>();
    }

    @Override
    public void clear() {
        this.registry.clear();
    }

    @Override
    public CrateReference createReference(Identifier crateId) {
        return new DefaultCrateReference(crateId, this);
    }

    @Override
    public void register(Crate crate) {
        this.registry.register(crate);
    }

    @Override
    public Set<Identifier> keys() {
        return this.registry.ids();
    }

    @Override
    public int size() {
        return this.registry.size();
    }

    @Override
    public Stream<Crate> stream() {
        return this.registry.stream();
    }

    @Override
    public Set<Crate> values() {
        return this.registry.values();
    }

    @Override
    public boolean contains(Identifier key) {
        return this.get(key) != null;
    }

    @Override
    public @Nullable Crate get(Identifier key) {
        return this.registry.get(key);
    }

    @Override
    public Optional<Crate> lookup(Identifier key) {
        return this.registry.lookup(key);
    }

    @Override
    public @Nullable Crate remove(Identifier id) {
        Crate removed = this.registry.get(id);
        if (removed != null) {
            this.remove(removed);
        }
        return removed;
    }

    @Override
    public void remove(Crate crate) {
        this.registry.remove(crate);
    }
}
