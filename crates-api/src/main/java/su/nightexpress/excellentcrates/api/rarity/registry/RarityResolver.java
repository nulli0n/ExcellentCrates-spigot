package su.nightexpress.excellentcrates.api.rarity.registry;

import java.util.Set;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;

@NullMarked
public interface RarityResolver {

    @Nullable
    Rarity resolveRarity(Identifier rarityId);

    Set<Rarity> rarities();

    boolean isEmpty();
}
