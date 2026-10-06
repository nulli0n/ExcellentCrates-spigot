package su.nightexpress.excellentcrates.integration.itemsadder;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.integration.itemsadder.block.ItemsAdderBlockPlaceController;
import su.nightexpress.excellentcrates.integration.itemsadder.block.ItemsAdderBlockProvider;
import su.nightexpress.excellentcrates.integration.itemsadder.block.ItemsAdderLoadController;

@NullMarked
public final class ItemsAdderAddonConfiguration {

    private static final Identifier MODULE_ID = new Identifier("itemsadder.module");

    private ItemsAdderAddonConfiguration() {
    }

    public static ItemsAdderAddonModule configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        ItemsAdderAddonModule module = new ItemsAdderAddonModule(MODULE_ID);
        ItemsAdderBlockProvider provider = new ItemsAdderBlockProvider();

        module.addComponent(new ItemsAdderLoadController(plugin, provider, blocksAPI));
        module.addComponent(new ItemsAdderBlockPlaceController(plugin, blocksAPI));

        return module;
    }
}
