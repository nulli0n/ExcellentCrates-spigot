package su.nightexpress.excellentcrates.keys.data.io;

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
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.extension.KeyDataExtension;
import su.nightexpress.excellentcrates.keys.data.key.DefaultCrateKey;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyBase;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyBuilder;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyDisplay;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyItem;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class KeyIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyIOService.class);

    private final Path                           keysDir;
    private final TinyRegistry<KeyDataExtension> extensions;

    public KeyIOService(Path keysDir, TinyRegistry<KeyDataExtension> extensions) {
        this.keysDir = keysDir;
        this.extensions = extensions;
    }

    public Path getKeyFile(CrateKey key) {
        return this.keysDir.resolve(FileConfig.withExtension(key.idString()));
    }

    public Optional<Path> getOrCreateKeyFile(CrateKey key) {
        Path file = this.getKeyFile(key);
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
                return Optional.of(Files.createFile(file));
            }
            catch (IOException exception) {
                LOGGER.error("Could not create key file '{}'", file);
                LOGGER.error("Reason: ", exception);
                return Optional.empty();
            }
        }

        return Optional.of(file);
    }

    public List<CrateKey> readKeys() {
        List<CrateKey> keys = new ArrayList<>();

        FileUtil.findYamlFiles(this.keysDir).forEach(file -> {
            try {
                keys.add(this.readKey(file));
            }
            catch (ModelLoadException exception) {
                LOGGER.error("Key '{}' can not be loaded.", file);
                LOGGER.error("Reason: ", exception);
            }
        });

        return keys;
    }

    public CrateKey readKey(Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);

        Identifier id = IdentifierParser.parseSanitized(name)
            .orElseThrow(() -> new ModelLoadException("Invalid file name"));

        FileConfig config = FileConfig.load(file);

        StandardKeyBase base = config.getOrSet("base", StandardKeyBase.class, StandardKeyBase.createDefault());
        StandardKeyDisplay display = config.getOrSet("display", StandardKeyDisplay.class, StandardKeyDisplay
            .createDefault());
        StandardKeyItem item = config.getOrSet("item", StandardKeyItem.class, StandardKeyItem.createDefault());

        StandardKeyBuilder builder = new StandardKeyBuilder(id);

        builder.base(base);
        builder.display(display);
        builder.item(item);

        this.extensions.getEntries().forEach(extension -> extension.onRead(config, builder));

        DefaultCrateKey key = builder.build();

        config.saveChanges();

        return key;
    }


    public boolean deleteKeyFile(CrateKey key) {
        Path file = this.getKeyFile(key);
        if (!Files.exists(file)) return true;

        try {
            return Files.deleteIfExists(file);
        }
        catch (IOException exception) {
            LOGGER.error("Key file '{}' can not be deleted.", file);
            LOGGER.error("Reason: ", exception);
            return false;
        }
    }

    public void writeKey(CrateKey key) {
        Path file = this.getOrCreateKeyFile(key).orElse(null);
        if (file == null) {
            LOGGER.error("Key '{}' can not be saved. See stracktrace above.", key.id());
            return;
        }

        FileConfig config = FileConfig.load(file);

        config.set("base", key.getBase());
        config.set("display", key.getDisplay());
        config.set("item", key.getItem());

        this.extensions.getEntries().forEach(extension -> extension.onWrite(config, key));

        config.saveChanges();
    }
}
