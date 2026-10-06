package su.nightexpress.excellentcrates.crates.registry;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.registry.CrateReference;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;

@NullMarked
public class DefaultCrateReference implements CrateReference {

    private final Identifier    id;
    private final CrateRegistry registry;

    public DefaultCrateReference(Identifier id, CrateRegistry registry) {
        this.id = id;
        this.registry = registry;
    }

    @Override
    public Identifier id() {
        return this.id;
    }

    @Override
    public @Nullable Crate get() {
        return this.registry.get(this.id);
    }
}