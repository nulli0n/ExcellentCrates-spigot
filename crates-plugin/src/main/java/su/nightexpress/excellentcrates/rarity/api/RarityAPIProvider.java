package su.nightexpress.excellentcrates.rarity.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.rarity.IRarityDataService;
import su.nightexpress.excellentcrates.api.rarity.RarityAPI;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityResolver;

@NullMarked
public class RarityAPIProvider implements RarityAPI {

    private final RarityRegistry     registry;
    private final IRarityDataService dataService;

    public RarityAPIProvider(RarityRegistry registry, IRarityDataService dataService) {
        this.registry = registry;
        this.dataService = dataService;
    }

    @Override
    public IRarityDataService getDataService() {
        return this.dataService;
    }

    @Override
    public RarityRegistry getRegistry() {
        return this.registry;
    }

    @Override
    public RarityResolver getResolver() {
        return this.registry;
    }
}
