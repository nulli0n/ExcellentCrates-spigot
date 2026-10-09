package su.nightexpress.excellentcrates.crates.block.vanilla;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.crates.block.vanilla.provider.VanillaBlockProvider;

@NullMarked
public final class VanillaBlockConfiguration {

    private static final Identifier ADDON_ID   = new Identifier("crates.blocks.addons.vanilla");
    private static final String     ADDON_NAME = "Vanilla Blocks";

    private VanillaBlockConfiguration() {
    }

    public static PluginComponent configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        VanillaBlockProvider provider = new VanillaBlockProvider();

        NamedComponentBundle bundle = new NamedComponentBundle(ADDON_ID, ADDON_NAME);

        blocksAPI.getRegistry().registerProvider(provider);
        bundle.addComponent(new VanillaBlockController(plugin, blocksAPI));

        return bundle;
    }
}
