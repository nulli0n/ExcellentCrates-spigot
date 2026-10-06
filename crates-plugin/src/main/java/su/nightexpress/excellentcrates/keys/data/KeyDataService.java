package su.nightexpress.excellentcrates.keys.data;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.data.extension.KeyDataExtension;
import su.nightexpress.excellentcrates.api.key.data.model.KeyBuilder;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.keys.data.io.KeyIOService;
import su.nightexpress.excellentcrates.keys.data.key.DefaultCrateKey;
import su.nightexpress.excellentcrates.keys.data.key.StandardKeyBuilder;

@NullMarked
public class KeyDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyDataService.class);

    private final KeyIOService ioService;
    private final KeyRegistry  registry;

    private final TinyRegistry<KeyDataExtension> extensions;

    private final Set<CrateKey> pendingSaves;

    public KeyDataService(KeyIOService ioService, KeyRegistry registry, TinyRegistry<KeyDataExtension> extensions) {
        this.ioService = ioService;
        this.registry = registry;
        this.extensions = extensions;

        this.pendingSaves = ConcurrentHashMap.newKeySet();
    }

    public void loadKeys() {
        this.ioService.readKeys().forEach(this::loadKey);

        LOGGER.info("Loaded {} keys.", this.registry.size());
    }

    public void loadKey(CrateKey key) {
        this.registry.register(key);

        this.extensions.getEntries().forEach(extension -> extension.onLoad(key));
    }

    public void unloadKeys() {
        this.saveDirty();

        this.registry.values().forEach(this::unloadKey);
    }

    public void unloadKey(CrateKey key) {
        this.registry.remove(key);

        this.extensions.getEntries().forEach(extension -> extension.onUnload(key));
    }

    public CrateKey createKey(Identifier id, Consumer<KeyBuilder> consumer) {
        if (this.hasKey(id)) {
            throw new IllegalArgumentException("Key with ID '" + id + "' already exists");
        }

        StandardKeyBuilder builder = new StandardKeyBuilder(id);

        consumer.accept(builder);

        this.extensions.getEntries().forEach(extension -> extension.onBuild(builder));

        DefaultCrateKey key = builder.build();

        this.extensions.getEntries().forEach(extension -> extension.onCreate(key));

        this.saveKey(key);
        this.loadKey(key);

        return key;
    }

    public void saveKey(CrateKey key) {
        this.pendingSaves.remove(key);
        this.ioService.writeKey(key);
    }

    public @Nullable CrateKey getKey(Identifier id) {
        return this.registry.get(id);
    }

    public boolean hasKey(Identifier id) {
        return this.registry.contains(id);
    }

    public boolean deleteKey(CrateKey key) {
        if (this.ioService.deleteKeyFile(key)) {
            this.unloadKey(key);

            this.extensions.getEntries().forEach(extension -> extension.onDelete(key));
            return true;
        }
        return false;
    }

    public void markDirty(CrateKey key) {
        this.pendingSaves.add(key);
    }

    public void saveDirty() {
        if (this.pendingSaves.isEmpty()) return;

        // Drain the queue safely
        Set<CrateKey> toSave = new HashSet<>(this.pendingSaves);
        this.pendingSaves.removeAll(toSave);

        for (CrateKey key : toSave) {
            this.ioService.writeKey(key);
        }
    }

    public void saveAll() {
        for (CrateKey key : this.registry.values()) {
            this.ioService.writeKey(key);
        }
    }
}
