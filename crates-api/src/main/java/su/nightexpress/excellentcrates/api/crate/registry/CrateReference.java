package su.nightexpress.excellentcrates.api.crate.registry;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateReference {

    Identifier id();

    @Nullable
    Crate get();

    default Optional<Crate> optional() {
        return Optional.ofNullable(this.get());
    }

    default Crate require() {
        Crate crate = this.get();
        if (crate == null) throw new IllegalStateException("Crate '" + this.id() + "' no longer exists.");
        return crate;
    }
}