package su.nightexpress.excellentcrates.api.rarity.registry;

import java.util.Optional;
import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.nightcore.bridge.registry.NRegistry;

@NullMarked
public interface RarityRegistry extends NRegistry<Identifier, Rarity>, RarityResolver {

    default boolean contains(Identifier id) {
        return this.get(id) != null;
    }

    default void remove(Rarity rarity) {
        this.remove(rarity.id());
    }

    @Override
    default @Nullable Rarity resolveRarity(Identifier rarityId) {
        return this.get(rarityId);
    }

    @Override
    default Set<Rarity> rarities() {
        return this.values();
    }

    Optional<Rarity> findMostWeighted();

    Optional<Rarity> findLessWeighted();
}
