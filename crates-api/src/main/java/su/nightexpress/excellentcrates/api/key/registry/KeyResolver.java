package su.nightexpress.excellentcrates.api.key.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;

@NullMarked
public interface KeyResolver {

    @Nullable
    CrateKey resolveKey(Identifier keyId);

    Set<CrateKey> keyValues();
}
