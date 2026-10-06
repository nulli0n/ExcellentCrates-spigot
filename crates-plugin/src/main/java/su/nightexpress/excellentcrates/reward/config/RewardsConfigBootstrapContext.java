package su.nightexpress.excellentcrates.reward.config;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.core.settings.FastSettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.reward.config.settings.RewardCoreSettings;
import su.nightexpress.excellentcrates.reward.config.settings.RewardModuleSettings;
import su.nightexpress.excellentcrates.reward.lang.RewardsLang;
import su.nightexpress.excellentcrates.reward.permission.RewardPerms;

@NullMarked
public class RewardsConfigBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.config");
    private static final String     NAME = "Config";

    private static final String SETTINGS_FILE_NAME = "rewards.core.yml";

    public final RewardModuleSettings                 modules;
    public final ReadOnlySettings<RewardCoreSettings> settings;

    public RewardsConfigBootstrapContext(CratesPlugin plugin) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);

        plugin.injectLang(RewardsLang.class);
        plugin.registerPermissions(RewardPerms.ROOT);

        SettingsProvider<RewardCoreSettings> provider = new SettingsProvider<>(RewardCoreSettings.defaults());
        this.addComponent(new FastSettingsController<>(settingsPath, RewardCoreSettings::loadFrom, provider));

        this.settings = provider;
        this.modules = this.settings.get().modules();
    }
}
