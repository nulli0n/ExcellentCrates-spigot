package su.nightexpress.excellentcrates.crates.block.vanilla.controller;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsLoader;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.block.vanilla.provider.VanillaBlockProvider;
import su.nightexpress.excellentcrates.crates.block.vanilla.settings.VanillaBlockSettings;

@NullMarked
public class VanillaBlockSettingsController extends SettingsController<VanillaBlockSettings> {

    private final BlockAPI             blocksAPI;
    private final VanillaBlockProvider blockProvider;

    public VanillaBlockSettingsController(Path path,
                                          SettingsLoader<VanillaBlockSettings> loader,
                                          SettingsProvider<VanillaBlockSettings> provider,
                                          BlockAPI blocksAPI,
                                          VanillaBlockProvider blockProvider) {
        super(path, loader, provider);
        this.blocksAPI = blocksAPI;
        this.blockProvider = blockProvider;
    }


    @Override
    public void start() {
        super.start();

        // Register only after the settings have been loaded, so that the provider has the correct blocks to register.
        this.blocksAPI.registerProviderWithBlocks(this.blockProvider);
    }

    @Override
    public void reload() {
        // Clean up currently registered blocks before reloading the settings.
        this.blocksAPI.unregisterBlocks(this.blockProvider);

        // Settings reload
        super.reload();

        // Register the blocks again after the settings have been reloaded.
        this.blockProvider.fetchBlocks().forEach(this.blocksAPI.getRegistry()::registerBlock);
    }
}
