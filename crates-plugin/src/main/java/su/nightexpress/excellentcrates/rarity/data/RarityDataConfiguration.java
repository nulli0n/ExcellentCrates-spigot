package su.nightexpress.excellentcrates.rarity.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.rarity.data.codec.RarityBaseCodec;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarityBase;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class RarityDataConfiguration {

    private static final String DIR_RARITY = "rarity";

    private RarityDataConfiguration() {
    }

    public static RarityDataService configure(CratesPlugin plugin, RarityRegistry registry) {
        ConfigCodecs.register(StandardRarityBase.class, RarityBaseCodec.INSTANCE);

        Path rarityDir = plugin.objectsPath().resolve(DIR_RARITY);
        RarityIOService ioService = new RarityIOService(rarityDir);
        RarityDataService dataService = new RarityDataService(ioService, registry);

        return dataService;
    }
}
