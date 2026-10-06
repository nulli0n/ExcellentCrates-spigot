package su.nightexpress.excellentcrates.crates.block.addon;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.crates.block.settings.BlockSettings;
import su.nightexpress.excellentcrates.crates.block.vanilla.VanillaBlockConfiguration;
import su.nightexpress.excellentcrates.integration.itemsadder.ItemsAdderAddonConfiguration;
import su.nightexpress.excellentcrates.integration.nexo.NexoAddonConfiguration;
import su.nightexpress.nightcore.util.Plugins;

@NullMarked
public final class BlockAddonConfiguration {

    private static final Identifier ID   = new Identifier("crates.blocks.addons");
    private static final String     NAME = "Addon Configuration";

    private static final Logger LOGGER = LoggerFactory.getLogger(BlockAddonConfiguration.class);

    private BlockAddonConfiguration() {
    }

    public static PluginComponent configure(CratesPlugin plugin, BlockAPI blocksAPI,
                                            ReadOnlySettings<BlockSettings> settings) {
        NamedComponentBundle bundle = new NamedComponentBundle(ID, NAME);

        if (isEnabled(settings, "vanilla")) {
            bundle.addComponent(VanillaBlockConfiguration.configure(plugin, blocksAPI));
        }

        if (isInstalled(BlockAddonPlugins.ITEMS_ADDER) && isEnabled(settings, "itemsadder")) {
            logPluginAddon(BlockAddonPlugins.ITEMS_ADDER);
            bundle.addComponent(ItemsAdderAddonConfiguration.configure(plugin, blocksAPI));
        }

        if (isInstalled(BlockAddonPlugins.NEXO) && isEnabled(settings, "nexo")) {
            logPluginAddon(BlockAddonPlugins.NEXO);
            bundle.addComponent(NexoAddonConfiguration.configure(plugin, blocksAPI));
        }

        return bundle;
    }

    private static boolean isEnabled(ReadOnlySettings<BlockSettings> settings, String provider) {
        return !settings.get().isDisabledProvider(provider);
    }

    private static boolean isInstalled(String pluginName) {
        return Plugins.isInstalled(pluginName);
    }

    private static void logPluginAddon(String pluginName) {
        LOGGER.info("Building '{}' block system addon.", pluginName);
    }
}
