package su.nightexpress.excellentcrates.integration.packetevents;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.hologram.HologramsAPI;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.integration.packetevents.controller.PlayerSessionController;
import su.nightexpress.excellentcrates.integration.packetevents.settings.HologramSettings;

@NullMarked
public final class PacketEventsHologramProviderConfiguration {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.holograms.packetevents");
    private static final String     BUNDLE_NAME = "PacketEvents Hologram Provider Configuration";

    private static final String SETTINGS_FILE_NAME = "crates.holograms.packetevents.yml";

    private PacketEventsHologramProviderConfiguration() {
    }

    public static PluginComponent configure(CratesPlugin plugin, HologramsAPI api) {
        NamedComponentBundle bundle = new NamedComponentBundle(BUNDLE_ID, BUNDLE_NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<HologramSettings> settings = new SettingsProvider<>(HologramSettings.defaults());

        HologramPacketManager handler = new HologramPacketManager(settings);
        PacketEventsHologramProvider provider = new PacketEventsHologramProvider(settings, handler);

        api.registerProvider(provider);

        bundle.addComponent(new SettingsController<>(settingsPath, HologramSettings::loadFrom, settings));
        bundle.addComponent(new PlayerSessionController(plugin, provider));

        return bundle;
    }
}
