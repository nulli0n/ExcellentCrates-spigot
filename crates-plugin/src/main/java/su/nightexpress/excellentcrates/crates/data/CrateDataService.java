package su.nightexpress.excellentcrates.crates.data;

import java.util.Set;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.data.DirtyTracker;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.data.extension.CrateDataExtension;
import su.nightexpress.excellentcrates.api.crate.data.model.CrateBuilder;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrateBuilder;
import su.nightexpress.excellentcrates.crates.data.crate.StandardCrate;
import su.nightexpress.excellentcrates.crates.data.io.CrateIOService;

@NullMarked
public class CrateDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CrateDataService.class);

    private final CrateIOService ioService;
    private final CrateRegistry  registry;

    private final TinyRegistry<CrateDataExtension> extensions;

    private final DirtyTracker<Identifier> dirtyTracker;

    public CrateDataService(CrateIOService ioService,
                            CrateRegistry registry,
                            TinyRegistry<CrateDataExtension> extensions) {
        this.ioService = ioService;
        this.registry = registry;

        this.extensions = extensions;

        this.dirtyTracker = new DirtyTracker<>();
    }

    public void loadCrates() {
        this.ioService.readCrates().forEach(this::loadCrate);

        LOGGER.info("Loaded {} crates.", this.registry.size());
    }

    public void loadCrate(Crate crate) {
        this.registry.register(crate);

        this.extensions.forEach(extension -> extension.onLoad(crate));
    }

    public void unloadCrates() {
        this.saveDirty();

        this.registry.values().forEach(this::unloadCrate);
    }

    public void unloadCrate(Crate crate) {
        this.registry.remove(crate);
        this.dirtyTracker.remove(crate.id());

        this.extensions.forEach(extension -> extension.onUnload(crate));
    }

    public Crate createCrate(Identifier id, Consumer<CrateBuilder> consumer) {
        if (this.hasCrate(id)) {
            throw new IllegalArgumentException("Crate with ID '" + id + "' already exists");
        }

        StandardCrateBuilder builder = new StandardCrateBuilder(id);

        consumer.accept(builder);

        this.extensions.forEach(extension -> extension.onBuild(builder, id));

        StandardCrate crate = builder.build();

        this.extensions.forEach(extension -> extension.onCreate(crate));

        this.saveCrate(crate);
        this.loadCrate(crate);

        return crate;
    }

    public void saveCrate(Crate crate) {
        this.dirtyTracker.remove(crate.id());
        this.ioService.writeCrate(crate);
    }

    public @Nullable Crate getCrate(Identifier id) {
        return this.registry.get(id);
    }

    public boolean hasCrate(Identifier id) {
        return this.registry.contains(id);
    }

    public void deleteCrate(Crate crate) {
        if (this.ioService.deleteCrateFile(crate)) {
            this.unloadCrate(crate);

            this.extensions.forEach(extension -> extension.onDelete(crate));
        }
    }

    public void markDirty(Crate crate) {
        this.dirtyTracker.add(crate.id());
    }

    public void saveDirty() {
        if (!this.dirtyTracker.hasAny()) return;

        // Drain the queue safely
        Set<Identifier> toSave = this.dirtyTracker.removeAndGetDirty();

        for (Identifier id : toSave) {
            Crate crate = this.registry.get(id);
            if (crate != null) {
                this.ioService.writeCrate(crate);
            }
        }
    }

    public void saveAll() {
        for (Crate crate : this.registry.values()) {
            this.ioService.writeCrate(crate);
        }
    }
}
