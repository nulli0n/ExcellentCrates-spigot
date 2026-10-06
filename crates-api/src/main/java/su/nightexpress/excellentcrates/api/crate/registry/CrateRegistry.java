package su.nightexpress.excellentcrates.api.crate.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.nightcore.bridge.registry.NRegistry;

@NullMarked
public interface CrateRegistry extends NRegistry<Identifier, Crate>, CrateResolver {

    @Override
    default @Nullable Crate resolveCrate(Identifier crateId) {
        return this.get(crateId);
    }

    @Override
    default Set<Crate> crates() {
        return this.values();
    }

    CrateReference createReference(Identifier crateId);

    default CrateReference createReference(Crate crate) {
        return this.createReference(crate.id());
    }

    boolean contains(Identifier key);

    void remove(Crate crate);
}
