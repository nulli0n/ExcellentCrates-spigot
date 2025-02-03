package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.inventory.InvOpeningProvider;
import su.nightexpress.excellentcrates.opening.world.WorldOpeningProvider;
import su.nightexpress.excellentcrates.opening.world.provider.DummyProvider;
import su.nightexpress.excellentcrates.opening.world.provider.SimpleRollProvider;
import su.nightexpress.nightcore.manager.AbstractManager;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.StringUtil;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class OpeningManager extends AbstractManager<CratesPlugin> {

    private final Map<String, OpeningProvider> providerByIdMap;
    private final Map<UUID, Opening>           openingByPlayerId;

    private final DummyProvider dummyProvider;

    public OpeningManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.providerByIdMap = new HashMap<>();
        this.openingByPlayerId = new ConcurrentHashMap<>();
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
        this.openingByPlayerId.clear();
    }

    public void loadDefaults() {
        for (OpeningType type : OpeningType.values()) {
            File dir = new File(this.getDirectory(type));
            if (dir.exists()) return;

            dir.mkdirs();

            switch (type) {
                case INVENTORY -> {
                    this.createInventoryProvider("csgo", OpeningUtils::setupCSGO);
                    this.createInventoryProvider("chests_full", OpeningUtils::setupChests);
                    this.createInventoryProvider("enclosing", OpeningUtils::setupEnclosing);
                    this.createInventoryProvider("mystery", OpeningUtils::setupMystery);
                    this.createInventoryProvider("roulette", OpeningUtils::setupRoulette);
                    this.createInventoryProvider("storm", OpeningUtils::setupStorm);
                }
                case WORLD -> {
                    this.createWorldProvider(SimpleRollProvider.ID, file -> new SimpleRollProvider(plugin, file), OpeningUtils::setupSimpleRoll);
                }
            }
        }
    }

    public void loadProviders() {
        this.loadInventoryProviders();
        this.loadWorldProviders();
        this.plugin.info("Loaded " + this.providerByIdMap.size() + " crate openings.");
    }

    public void loadInventoryProviders() {
        for (File file : FileUtil.getFiles(this.getDirectory(OpeningType.INVENTORY))) {
            this.loadInventoryProvider(file);
        }
    }

    public void loadWorldProviders() {
        this.loadWorldProvider(SimpleRollProvider.ID, file -> new SimpleRollProvider(plugin, file));
    }

    public boolean loadInventoryProvider(@NotNull File file) {
        InvOpeningProvider provider = new InvOpeningProvider(plugin, file);
        if (!provider.load()) {
            this.plugin.error("Inventory opening provider not loaded: '" + file.getName() + "'.");
            return false;
        }

        this.loadProvider(provider.getId(), provider);
        return true;
    }

    public <T extends WorldOpeningProvider> boolean loadWorldProvider(@NotNull String id, @NotNull Function<File, T> function) {
        File file = new File(this.getDirectory(OpeningType.WORLD), id + ".yml");
        T provider = function.apply(file);

        if (!provider.load()) {
            this.plugin.error("World opening provider not loaded: '" + file.getName() + "'.");
            return false;
        }

        this.loadProvider(provider.getId(), provider);
        return true;
    }

    public void createInventoryProvider(@NotNull String id, @NotNull Consumer<InvOpeningProvider> consumer) {
        InvOpeningProvider provider = this.createProvider(id, OpeningType.INVENTORY, file -> new InvOpeningProvider(plugin, file), consumer);
        if (provider == null) return;

        provider.save();
    }

    public <T extends WorldOpeningProvider> void createWorldProvider(@NotNull String id, @NotNull Function<File, T> function, @NotNull Consumer<T> consumer) {
        T provider = this.createProvider(id, OpeningType.WORLD, function, consumer);
        if (provider == null) return;

        provider.save();
    }

    @Nullable
    private <T extends OpeningProvider> T createProvider(@NotNull String id,
                                                         @NotNull OpeningType type,
                                                         @NotNull Function<File, T> function,
                                                         @NotNull Consumer<T> consumer) {
        id = StringUtil.transformForID(id);
        if (id.isBlank()) return null;

        File file = new File(this.getDirectory(type), id + ".yml");
        T provider = function.apply(file);
        consumer.accept(provider);
        return provider;
    }

    @NotNull
    public String getDirectory(@NotNull OpeningType type) {
        return plugin.getDataFolder() + switch (type) {
            case WORLD -> Config.DIR_OPENINGS_WORLD;
            case INVENTORY -> Config.DIR_OPENINGS_GUI;
        };
    }

    public void loadProvider(@NotNull String id, @NotNull OpeningProvider provider) {
        this.providerByIdMap.put(id.toLowerCase(), provider);
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
        return this.openingByPlayerId;
    }

    @NotNull
    public Set<Opening> getOpenings() {
        return new HashSet<>(this.openingByPlayerId.values());
    }

    @Nullable
    public Opening getOpening(@NotNull Player player) {
        return this.openingByPlayerId.get(player.getUniqueId());
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
        return this.openingByPlayerId.remove(player.getUniqueId());
    }

    public boolean isOpeningAvailable(@NotNull Player player, @NotNull CrateSource source) {
        return !this.isOpening(player);
    }

    @NotNull
    public Opening createOpening(@NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        Crate crate = source.getCrate();
        OpeningProvider provider = null;

        if (crate.isAnimationEnabled()) {
            provider = this.getProviderById(crate.getAnimationId());
        }
        if (provider == null) provider = this.dummyProvider;

        return provider.createOpening(player, source, key);
    }

    public void startOpening(@NotNull Player player, @NotNull Opening opening, boolean instaRoll) {
        this.openingByPlayerId.putIfAbsent(player.getUniqueId(), opening);

        opening.run(); // Start ticking

        if (instaRoll) opening.instaRoll();
    }
}
