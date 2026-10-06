package su.nightexpress.excellentcrates.engine.config;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;

@NullMarked
public class PluginConfigBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("engine.config");
    private static final String     NAME = "Plugin Settings";

    private static final String SETTINGS_FILE_NAME = "engine.core.yml";

    public final ReadOnlySettings<PluginSettings> settings;

    public PluginConfigBootstrapContext(CratesPlugin plugin) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<PluginSettings> provider = new SettingsProvider<>(PluginSettings.defaults());

        this.settings = provider;
        this.addComponent(new SettingsController<>(settingsPath, PluginSettings::loadFrom, provider));
    }
}
