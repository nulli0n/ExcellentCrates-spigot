package su.nightexpress.excellentcrates.rarity.io;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.excellentcrates.api.CratesPlugin;

@NullMarked
public final class RarityIOConfiguration {

    private static final String DIR_RARITY = "rarity";

    private RarityIOConfiguration() {
    }

    public static RarityIOService configure(CoreDependencies coreDependencies) {
        CratesPlugin plugin = coreDependencies.plugin();

        Path rarityDir = plugin.objectsPath().resolve(DIR_RARITY);
        return new RarityIOService(rarityDir);
    }
}
