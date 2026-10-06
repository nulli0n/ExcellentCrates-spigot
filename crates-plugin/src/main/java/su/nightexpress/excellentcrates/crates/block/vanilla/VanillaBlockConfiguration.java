package su.nightexpress.excellentcrates.crates.block.vanilla;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.block.BlockAPI;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.block.vanilla.block.VanillaBlockDefinition;
import su.nightexpress.excellentcrates.crates.block.vanilla.codec.VanillaBlockDefinitionCodec;
import su.nightexpress.excellentcrates.crates.block.vanilla.controller.VanillaBlockSettingsController;
import su.nightexpress.excellentcrates.crates.block.vanilla.provider.VanillaBlockProvider;
import su.nightexpress.excellentcrates.crates.block.vanilla.settings.VanillaBlockSettings;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public final class VanillaBlockConfiguration {

    private static final Identifier ADDON_ID   = new Identifier("crates.blocks.addons.vanilla");
    private static final String     ADDON_NAME = "Vanilla Blocks";

    private static final String SETTINGS_FILE = "crates.blocks.vanilla.yml";

    private VanillaBlockConfiguration() {
    }

    public static PluginComponent configure(CratesPlugin plugin, BlockAPI blocksAPI) {
        ConfigCodecs.register(VanillaBlockDefinition.class, VanillaBlockDefinitionCodec.INSTANCE);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE);
        SettingsProvider<VanillaBlockSettings> settingsProvider = new SettingsProvider<>(VanillaBlockSettings.empty());

        VanillaBlockProvider provider = new VanillaBlockProvider(settingsProvider);

        NamedComponentBundle bundle = new NamedComponentBundle(ADDON_ID, ADDON_NAME);

        bundle.addComponent(new VanillaBlockSettingsController(
            settingsPath,
            VanillaBlockSettings::loadFrom,
            settingsProvider,
            blocksAPI,
            provider
        ));

        bundle.addComponent(new VanillaBlockController(plugin, blocksAPI));

        return bundle;
    }
}
