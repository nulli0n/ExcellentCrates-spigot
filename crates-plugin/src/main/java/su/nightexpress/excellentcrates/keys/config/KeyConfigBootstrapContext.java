package su.nightexpress.excellentcrates.keys.config;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.core.settings.FastSettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;

@NullMarked
public class KeyConfigBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.config");
    private static final String     NAME = "Config";

    private static final String SETTINGS_FILE_NAME = "keys.core.yml";

    public final ReadOnlySettings<KeyCoreSettings> settings;

    public KeyConfigBootstrapContext(CratesPlugin plugin) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<KeyCoreSettings> provider = new SettingsProvider<>(KeyCoreSettings.defaults());

        this.settings = provider;

        this.addComponent(FastSettingsController.createAndLoad(settingsPath, KeyCoreSettings::loadFrom, provider));
    }
}
