package su.nightexpress.excellentcrates.integration.itemsadder;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.integration.itemsadder.block.ItemsAdderBlockController;
import su.nightexpress.excellentcrates.integration.itemsadder.block.ItemsAdderBlockProvider;

@NullMarked
public final class ItemsAdderAddonConfiguration {

    private static final Identifier MODULE_ID = new Identifier("crates.blocks.addon.itemsadder");

    private ItemsAdderAddonConfiguration() {
    }

    public static ItemsAdderAddonModule configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        ItemsAdderAddonModule module = new ItemsAdderAddonModule(MODULE_ID);
        ItemsAdderBlockProvider provider = new ItemsAdderBlockProvider();

        blocksAPI.getRegistry().registerProvider(provider);

        module.addComponent(new ItemsAdderBlockController(plugin, blocksAPI));

        return module;
    }
}
