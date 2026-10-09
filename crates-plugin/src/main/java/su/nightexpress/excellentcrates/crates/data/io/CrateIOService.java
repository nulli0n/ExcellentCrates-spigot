package su.nightexpress.excellentcrates.crates.data.io;

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
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateBase;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateBuilder;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateDisplay;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateItem;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrate;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class CrateIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateIOService.class);

    private final Path cratesDir;

    private final TinyRegistry<CrateDataExtension> extensions;

    public CrateIOService(Path cratesDir, TinyRegistry<CrateDataExtension> extensions) {
        this.cratesDir = cratesDir;
        this.extensions = extensions;
    }

    public Path getCrateFile(Crate crate) {
        return this.cratesDir.resolve(FileConfig.withExtension(crate.idString()));
    }

    public Optional<Path> getOrCreateCrateFile(Crate crate) {
        Path file = this.getCrateFile(crate);
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
                return Optional.of(Files.createFile(file));
            }
            catch (IOException exception) {
                LOGGER.error("Could not create crate file '{}'", file);
                LOGGER.error("Reason: ", exception);
                return Optional.empty();
            }
        }

        return Optional.of(file);
    }

    public List<Crate> readCrates() {
        List<Crate> crates = new ArrayList<>();

        FileUtil.findYamlFiles(this.cratesDir).forEach(file -> {
            try {
                crates.add(this.readCrate(file));
            }
            catch (ModelLoadException exception) {
                LOGGER.error("Crate '{}' can not be loaded.", file);
                LOGGER.error("Reason: ", exception);
            }
        });

        return crates;
    }

    public Crate readCrate(Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);

        Identifier id = IdentifierParser.parseSanitized(name)
            .orElseThrow(() -> new ModelLoadException("Invalid file name"));

        FileConfig config = FileConfig.load(file);

        StandardCrateBase base = config.getOrSet("base", StandardCrateBase.class, StandardCrateBase.createDefault());
        StandardCrateDisplay display = config.getOrSet("display", StandardCrateDisplay.class, StandardCrateDisplay
            .createDefault());
        StandardCrateItem item = config.getOrSet("item", StandardCrateItem.class, StandardCrateItem.createDefault());

        StandardCrateBuilder builder = new StandardCrateBuilder(id);

        builder.base(base);
        builder.display(display);
        builder.item(item);

        this.extensions.getEntries().forEach(extension -> {
            extension.onRead(config, builder, id);
        });

        StandardCrate crate = builder.build();

        config.saveChanges();

        return crate;
    }


    public boolean deleteCrateFile(Crate crate) {
        Path file = this.getCrateFile(crate);
        if (!Files.exists(file)) return true;

        try {
            return Files.deleteIfExists(file);
        }
        catch (IOException exception) {
            LOGGER.error("Crate file '{}' can not be deleted.", file);
            LOGGER.error("Reason: ", exception);
            return false;
        }
    }

    public void writeCrate(Crate crate) {
        Path file = this.getOrCreateCrateFile(crate).orElse(null);
        if (file == null) {
            LOGGER.error("Crate '{}' can not be saved. See stracktrace above.", crate.id());
            return;
        }

        FileConfig config = FileConfig.load(file);

        config.set("base", crate.getBase());
        config.set("display", crate.getDisplay());
        config.set("item", crate.getItem());

        this.extensions.getEntries().forEach(extension -> {
            extension.onWrite(config, crate);
        });

        config.saveChanges();
    }
}
