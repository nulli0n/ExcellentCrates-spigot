package su.nightexpress.excellentcrates.api.crate.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateResolver {

    @Nullable
    Crate resolveCrate(Identifier crateId);

    Set<Crate> crates();
}
