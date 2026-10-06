package su.nightexpress.excellentcrates.crates.config;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.core.settings.FastSettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.config.settings.CrateCoreSettings;
import su.nightexpress.excellentcrates.crates.config.settings.CrateModuleSettings;

@NullMarked
public class CratesConfigBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.config");
    private static final String     NAME = "Config";

    private static final String SETTINGS_FILE_NAME = "crates.core.yml";

    public final ReadOnlySettings<CrateCoreSettings> settings;
    public final CrateModuleSettings                 modules;

    public CratesConfigBootstrapContext(CratesPlugin plugin) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<CrateCoreSettings> provider = new SettingsProvider<>(CrateCoreSettings.defaults());
        this.addComponent(new FastSettingsController<>(settingsPath, CrateCoreSettings::loadFrom, provider));

        this.settings = provider;
        this.modules = provider.get().modules();
    }
}
