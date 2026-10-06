package su.nightexpress.excellentcrates.integration.nexo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.integration.nexo.block.NexoBlockController;
import su.nightexpress.excellentcrates.integration.nexo.block.NexoBlockProvider;
import su.nightexpress.excellentcrates.integration.nexo.block.NexoDataLoadController;

@NullMarked
public final class NexoAddonConfiguration {

    private static final Identifier MODULE_ID = new Identifier("nexo-addon");

    private NexoAddonConfiguration() {
    }

    public static NexoAddonModule configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        NexoAddonModule module = new NexoAddonModule(MODULE_ID);
        NexoBlockProvider provider = new NexoBlockProvider();

        module.addComponent(new NexoDataLoadController(plugin, provider, blocksAPI));
        module.addComponent(new NexoBlockController(plugin, blocksAPI));

        return module;
    }
}
