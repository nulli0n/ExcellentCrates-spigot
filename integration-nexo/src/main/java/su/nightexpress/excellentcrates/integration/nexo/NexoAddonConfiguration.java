package su.nightexpress.excellentcrates.integration.nexo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.integration.nexo.block.NexoBlockController;
import su.nightexpress.excellentcrates.integration.nexo.block.NexoBlockProvider;

@NullMarked
public final class NexoAddonConfiguration {

    private static final Identifier MODULE_ID = new Identifier("crates.blocks.addon.nexo");

    private NexoAddonConfiguration() {
    }

    public static NexoAddonModule configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        NexoAddonModule module = new NexoAddonModule(MODULE_ID);
        NexoBlockProvider provider = new NexoBlockProvider();

        blocksAPI.getRegistry().registerProvider(provider);

        module.addComponent(new NexoBlockController(plugin, blocksAPI));

        return module;
    }
}
