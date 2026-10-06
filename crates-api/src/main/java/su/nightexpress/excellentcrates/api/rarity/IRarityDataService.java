package su.nightexpress.excellentcrates.api.rarity;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public interface IRarityDataService {

    void loadRarities();

    void loadRarity(Rarity rarity);

    void unloadRarities();

    void unloadRarity(Rarity rarity);

    void saveRarity(Rarity rarity);

    @Nullable
    Rarity getRarity(Identifier id);

    Rarity createRarity(Identifier id);

    void deleteRarity(Rarity rarity);

    void markDirty(Rarity rarity);

    void saveDirty();

    void saveAll();
}
