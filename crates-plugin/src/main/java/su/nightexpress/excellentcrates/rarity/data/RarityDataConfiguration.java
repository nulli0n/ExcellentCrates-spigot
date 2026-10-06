package su.nightexpress.excellentcrates.rarity.data;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.rarity.data.codec.RarityBaseCodec;
import su.nightexpress.excellentcrates.rarity.data.rarity.StandardRarityBase;
import su.nightexpress.excellentcrates.rarity.io.RarityIOService;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class RarityDataConfiguration {

    private RarityDataConfiguration() {
    }

    public static RarityDataService configure(RarityRegistry repository, RarityIOService ioService) {
        ConfigCodecs.register(StandardRarityBase.class, RarityBaseCodec.INSTANCE);

        RarityDataService dataService = new RarityDataService(ioService, repository);

        return dataService;
    }
}
