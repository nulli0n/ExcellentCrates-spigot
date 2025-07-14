package su.nightexpress.excellentcrates.opening;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.opening.OpeningProvider;
import su.nightexpress.excellentcrates.api.opening.ProviderLoader;
import su.nightexpress.excellentcrates.api.opening.ProviderSupplier;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.opening.inventory.InventoryProvider;
import su.nightexpress.excellentcrates.opening.selectable.SelectableProvider;
import su.nightexpress.excellentcrates.opening.world.provider.SimpleRollProvider;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProviderRegistry {

    private static final Map<String, ProviderLoader> LOADERS   = new HashMap<>();
    private static final Set<OpeningProvider>        PROVIDERS = new HashSet<>();

    public static void load() {
        ProviderRegistry.registerLoader(Config.DIR_OPENINGS_INVENTORY, InventoryProvider::new);
        ProviderRegistry.registerLoader(Config.DIR_OPENINGS_SIMPLE_ROLL, SimpleRollProvider::new);
        ProviderRegistry.registerLoader(Config.DIR_OPENINGS_SELECTABLE, SelectableProvider::new);
    }

    public static void clear() {
        LOADERS.clear();
        PROVIDERS.clear();
    }

    public static void registerLoader(@NotNull String directory, @NotNull ProviderSupplier supplier) {
        LOADERS.put(directory.toLowerCase(), new ProviderLoader(directory, supplier));
    }

    public static void registerProvider(@NotNull OpeningProvider provider) {
        PROVIDERS.add(provider);
    }

    @NotNull
    public static Set<ProviderLoader> getLoaders() {
        return new HashSet<>(LOADERS.values());
    }

    @NotNull
    public static Set<OpeningProvider> getProviders() {
        return new HashSet<>(PROVIDERS);
    }
}
