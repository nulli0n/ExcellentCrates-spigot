package su.nightexpress.excellentcrates.api.rarity;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityResolver;

@NullMarked
public interface RarityAPI extends PluginAPI {

    RarityRegistry getRegistry();

    RarityResolver getResolver();

    IRarityDataService getDataService();
}
