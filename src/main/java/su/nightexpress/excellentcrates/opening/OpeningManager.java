package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.api.opening.ProviderLoader;
import su.nightexpress.excellentcrates.api.opening.ProviderSupplier;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.world.provider.DummyProvider;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.FileUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OpeningManager extends AbstractManager<CratesPlugin> {

    private final Map<String, OpeningProvider> providerByIdMap;
    private final Map<UUID, Opening>           openingByPlayerMap;

    private final DummyProvider dummyProvider;

    public OpeningManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.providerByIdMap = new HashMap<>();
        this.openingByPlayerMap = new ConcurrentHashMap<>();
        this.dummyProvider = new DummyProvider(plugin);
    }

    @Override
    protected void onLoad() {
        this.loadDefaults();
        this.loadProviders();

        this.addListener(new OpeningListener(this.plugin, this));

        this.addTask(this::tickOpenings, 1L);
    }

    @Override
    protected void onShutdown() {
        this.getOpenings().forEach(Opening::stop);
        this.providerByIdMap.clear();
        this.openingByPlayerMap.clear();
    }

    @NotNull
    private String getDirectoryPath(@NotNull String dirName) {
        return this.plugin.getDataFolder() + dirName;
    }

    private void loadDefaults() {
        File dir = new File(this.getDirectoryPath(Config.DIR_OPENINGS));
        if (dir.exists()) return;

        this.loadProvider("csgo", Config.DIR_OPENINGS_INVENTORY, OpeningUtils::setupCSGO);
        this.loadProvider("enclosing", Config.DIR_OPENINGS_INVENTORY, OpeningUtils::setupEnclosing);
        this.loadProvider("mystery", Config.DIR_OPENINGS_INVENTORY, OpeningUtils::setupMystery);
        this.loadProvider("roulette", Config.DIR_OPENINGS_INVENTORY, OpeningUtils::setupRoulette);
        this.loadProvider("storm", Config.DIR_OPENINGS_INVENTORY, OpeningUtils::setupStorm);

        this.loadProvider("simple_roll", Config.DIR_OPENINGS_SIMPLE_ROLL, OpeningUtils::createSimpleRoll);

        this.loadProvider("selective_1", Config.DIR_OPENINGS_SELECTABLE, OpeningUtils::createSelectableSingle);
        this.loadProvider("selective_3", Config.DIR_OPENINGS_SELECTABLE, OpeningUtils::createSelectableTriple);
    }

    public void loadProviders() {
        // Load providers stored in the openings directory by native or externally added loaders.
        for (ProviderLoader loader : ProviderRegistry.getLoaders()) {
            this.loadProviders(loader);
        }

        // Load externally added independend opening providers.
        for (OpeningProvider provider : ProviderRegistry.getProviders()) {
            this.loadProvider(provider);
        }

        this.plugin.info("Loaded " + this.providerByIdMap.size() + " crate openings.");
    }

    public void loadProviders(@NotNull ProviderLoader loader) {
        String dirName = loader.getDirectory();
        ProviderSupplier supplier = loader.getSupplier();

        for (File file : FileUtil.getConfigFiles(this.getDirectoryPath(dirName))) {
            this.loadProvider(file, supplier);
        }
    }

    public void loadProvider(@NotNull String id, @NotNull String dirName, @NotNull ProviderSupplier supplier) {
        File file = new File(this.getDirectoryPath(dirName), FileConfig.withExtension(id));
        this.loadProvider(file, supplier);
    }

    public void loadProvider(@NotNull File file, @NotNull ProviderSupplier supplier) {
        FileConfig config = new FileConfig(file);
        String name = FileConfig.getName(file);

        OpeningProvider provider = supplier.supply(this.plugin, name);
        provider.load(config);
        config.saveChanges();

        this.loadProvider(provider);
    }

    public void loadProvider(@NotNull OpeningProvider provider) {
        this.providerByIdMap.put(provider.getId(), provider);
    }

    @NotNull
    public Map<String, OpeningProvider> getProviderByIdMap() {
        return this.providerByIdMap;
    }

    @NotNull
    public Set<OpeningProvider> getProviders() {
        return new HashSet<>(this.providerByIdMap.values());
    }

    @NotNull
    public Set<String> getProviderIds() {
        return new HashSet<>(this.providerByIdMap.keySet());
    }

    @Nullable
    public OpeningProvider getProviderById(@NotNull String id) {
        return this.providerByIdMap.get(id.toLowerCase());
    }

    @NotNull
    public Map<UUID, Opening> getOpeningByPlayerIdMap() {
        return this.openingByPlayerMap;
    }

    @NotNull
    public Set<Opening> getOpenings() {
        return new HashSet<>(this.openingByPlayerMap.values());
    }

    @Nullable
    public Opening getOpening(@NotNull Player player) {
        return this.openingByPlayerMap.get(player.getUniqueId());
    }

    public void tickOpenings() {
        this.getOpenings().forEach(Opening::tick);
    }

    public boolean isOpening(@NotNull Player player) {
        return this.getOpening(player) != null;
    }

    public void stopOpening(@NotNull Player player) {
        Opening opening = this.removeOpening(player);
        if (opening == null) return;

        opening.stop();
    }

    @Nullable
    public Opening removeOpening(@NotNull Player player) {
        return this.openingByPlayerMap.remove(player.getUniqueId());
    }

    public boolean isOpeningAvailable(@NotNull Player player) {
        return !this.isOpening(player);
    }

    @NotNull
    public Opening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        Crate crate = source.getCrate();
        OpeningProvider provider = null;

        if (crate.isOpeningEnabled()) {
            provider = this.getProviderById(crate.getOpeningId());
        }
        if (provider == null) provider = this.dummyProvider;

        return provider.createOpening(player, source, cost);
    }

    public void startOpening(@NotNull Player player, @NotNull Opening opening, boolean instaRoll) {
        this.openingByPlayerMap.putIfAbsent(player.getUniqueId(), opening);

        opening.start(); // Start ticking

        if (instaRoll) opening.instaRoll();
    }
}
