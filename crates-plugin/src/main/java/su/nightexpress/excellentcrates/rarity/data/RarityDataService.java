package su.nightexpress.excellentcrates.rarity.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Color;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.IRarityDataService;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarity;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarityBase;
import su.nightexpress.excellentcrates.rarity.io.RarityIOService;
import su.nightexpress.nightcore.util.text.night.wrapper.TagWrappers;

@NullMarked
public class RarityDataService implements IRarityDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RarityDataService.class);

    private final RarityIOService ioService;
    private final RarityRegistry  repository;

    private final Set<Rarity> pendingSaves;

    public RarityDataService(RarityIOService ioService, RarityRegistry repository) {
        this.ioService = ioService;
        this.repository = repository;

        this.pendingSaves = ConcurrentHashMap.newKeySet();
    }

    public void saveDefaultRarities() {
        List<Rarity> rarities = new ArrayList<>();

        rarities.add(new StandardRarity(
            new Identifier("common"),
            new StandardRarityBase(TagWrappers.WHITE.wrap("Common"), 0, Color.WHITE)
        ));

        rarities.add(new StandardRarity(
            new Identifier("uncommon"),
            new StandardRarityBase(TagWrappers.GREEN.wrap("Uncommon"), 1, Color.GREEN)
        ));

        rarities.add(new StandardRarity(
            new Identifier("rare"),
            new StandardRarityBase(TagWrappers.BLUE.wrap("Rare"), 2, Color.BLUE)
        ));

        rarities.add(new StandardRarity(
            new Identifier("epic"),
            new StandardRarityBase(TagWrappers.LIGHT_PURPLE.wrap("Epic"), 3, Color.PURPLE)
        ));

        rarities.add(new StandardRarity(
            new Identifier("legendary"),
            new StandardRarityBase(TagWrappers.GOLD.wrap("Legendary"), 4, Color.ORANGE)
        ));

        rarities.forEach(rarity -> this.ioService.writeRarity(rarity));
    }

    @Override
    public void loadRarities() {
        if (!this.ioService.hasRarities()) {
            LOGGER.info("No rarities found, saving default rarities.");
            this.saveDefaultRarities();
        }

        this.ioService.readRarities().forEach(this::loadRarity);
        LOGGER.info("Loaded {} rarities.", this.repository.size());
    }

    @Override
    public void loadRarity(Rarity rarity) {
        this.repository.register(rarity);
    }

    @Override
    public void unloadRarities() {
        this.repository.values().forEach(this::unloadRarity);
    }

    @Override
    public void unloadRarity(Rarity rarity) {
        this.repository.remove(rarity);
    }

    @Override
    public void saveRarity(Rarity rarity) {
        this.pendingSaves.remove(rarity);
        this.ioService.writeRarity(rarity);
    }

    @Override
    public @Nullable Rarity getRarity(Identifier id) {
        return this.repository.get(id);
    }

    @Override
    public Rarity createRarity(Identifier id) {
        StandardRarityBase base = StandardRarityBase.createDefault();
        StandardRarity rarity = new StandardRarity(id, base);
        this.loadRarity(rarity);
        this.saveRarity(rarity);

        return rarity;
    }

    @Override
    public void deleteRarity(Rarity rarity) {
        if (this.ioService.deleteRarityFile(rarity.id())) {
            this.unloadRarity(rarity);
        }
    }

    @Override
    public void markDirty(Rarity rarity) {
        this.pendingSaves.add(rarity);
    }

    @Override
    public void saveDirty() {
        if (this.pendingSaves.isEmpty()) return;

        // Drain the queue safely
        Set<Rarity> toSave = new HashSet<>(this.pendingSaves);
        this.pendingSaves.removeAll(toSave);

        for (Rarity rarity : toSave) {
            this.ioService.writeRarity(rarity);
        }
    }

    @Override
    public void saveAll() {
        for (Rarity rarity : this.repository.values()) {
            this.ioService.writeRarity(rarity);
        }
    }
}
