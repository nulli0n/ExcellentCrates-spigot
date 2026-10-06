package su.nightexpress.excellentcrates.api;

import org.bukkit.plugin.ServicePriority;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.crate.CratesAPI;

@NullMarked
public class PluginAPIConfiguration {

    private PluginAPIConfiguration() {
    }

    public static void configure(CratesPlugin plugin, ServiceRegistry registry) {
        CratesAPI cratesAPI = registry.require(CoreServices.CRATES);

        StandardExcellentCratesAPI.Builder builder = new StandardExcellentCratesAPI.Builder(cratesAPI)
            .setCostAPI(registry.get(CoreServices.COST))
            .setKeysAPI(registry.get(CoreServices.KEYS))
            .setPreviewAPI(registry.get(CoreServices.PREVIEW))
            .setRarityAPI(registry.get(CoreServices.RARITY))
            .setRewardsAPI(registry.get(CoreServices.REWARDS));

        StandardExcellentCratesAPI api = builder.build();

        plugin.getServer().getServicesManager().register(ExcellentCratesAPI.class, api, plugin, ServicePriority.Normal);
    }
}
