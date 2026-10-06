package su.nightexpress.excellentcrates.api.key.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.bridge.registry.NRegistry;

@NullMarked
public interface KeyRegistry extends NRegistry<Identifier, CrateKey>, KeyResolver {

    @Override
    default Set<CrateKey> keyValues() {
        return this.values();
    }

    @Override
    default @Nullable CrateKey resolveKey(Identifier keyId) {
        return this.get(keyId);
    }

    boolean contains(Identifier key);

    void remove(CrateKey key);

    void reindex(CrateKey key);
}
