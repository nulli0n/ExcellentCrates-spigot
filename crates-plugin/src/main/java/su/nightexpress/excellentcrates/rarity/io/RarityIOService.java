package su.nightexpress.excellentcrates.rarity.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarity;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarityBase;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class RarityIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RarityIOService.class);

    private final Path rarityDir;

    public RarityIOService(Path rarityFile) {
        this.rarityDir = rarityFile;
    }

    public Path getRarityFile(StandardRarity rarity) {
        return this.getRarityFile(rarity.id());
    }

    public Path getRarityFile(Identifier id) {
        return this.rarityDir.resolve(FileConfig.withExtension(id.value()));
    }

    public boolean hasRarities() {
        return !FileUtil.findYamlFiles(this.rarityDir).isEmpty();
    }

    public List<Rarity> readRarities() {
        List<Rarity> rarities = new ArrayList<>();

        FileUtil.findYamlFiles(this.rarityDir).forEach(file -> {
            try {
                rarities.add(this.readRarity(file));
            }
            catch (ModelLoadException exception) {
                LOGGER.error("Rarity '{}' can not be loaded.", file);
                LOGGER.error("Reason: ", exception);
            }
        });

        return rarities;
    }

    public Rarity readRarity(Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);
        Identifier id = IdentifierParser.parse(name)
            .orElseThrow(() -> new ModelLoadException("Invalid rarity identifier"));

        FileConfig config = FileConfig.load(file);

        StandardRarityBase base = config.getOrSet("base", StandardRarityBase.class, StandardRarityBase.createDefault());

        config.saveChanges();

        return new StandardRarity(id, base);
    }

    public void writeRarity(Rarity rarity) {
        Path file = this.createRarityFile(rarity.id()).orElse(null);
        if (file == null) {
            LOGGER.error("Rarity '{}' can not be saved.", rarity.id());
            return;
        }

        FileConfig config = FileConfig.load(file);

        config.set("base", rarity.getBase());
        config.save();
    }

    public Optional<Path> createRarityFile(Identifier id) {
        Path file = this.getRarityFile(id);
        if (Files.exists(file)) return Optional.of(file);

        try {
            Files.createDirectories(file.getParent());
            return Optional.of(Files.createFile(file));
        }
        catch (IOException exception) {
            LOGGER.error("Rarity file '{}' can not be created.", file);
            LOGGER.error("Reason: ", exception);
            return Optional.empty();
        }
    }

    public boolean deleteRarityFile(Identifier id) {
        Path file = this.getRarityFile(id);
        try {
            return Files.deleteIfExists(file);
        }
        catch (IOException exception) {
            LOGGER.error("Rarity file '{}' can not be deleted.", file);
            LOGGER.error("Reason: ", exception);
            return false;
        }
    }
}
