package su.nightexpress.excellentcrates.reward.preview;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.reward.preview.extension.RewardPreviewDataExtension;
import su.nightexpress.excellentcrates.reward.preview.placeholder.RewardPreviewPlaceholder;
import su.nightexpress.excellentcrates.reward.preview.settings.RewardPreviewSettings;

@NullMarked
public class RewardPreviewBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.preview");
    private static final String     NAME = "Reward Preview";

    private static final String SETTINGS_FILE_NAME = "rewards.preview.yml";

    public final RewardPreviewService  previewService;
    public final RewardPreviewResolver previewResolver;

    private final RewardPreviewPlaceholder   placeholder;
    private final RewardPreviewDataExtension dataExtension;

    public final DefaultRewardPreviewAPI api;

    public RewardPreviewBootstrapContext(CratesPlugin plugin, RewardPlaceholders placeholders) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<RewardPreviewSettings> settings = new SettingsProvider<>(RewardPreviewSettings.defaults());
        SettingsController<RewardPreviewSettings> settingsController = new SettingsController<>(
            settingsPath, RewardPreviewSettings::loadFrom, settings
        );
        settingsController.loadSettings();

        this.previewResolver = new RewardPreviewResolver(settings.get().previewCacheTTL(), TimeUnit.MINUTES);
        this.previewService = new RewardPreviewService(settings, this.previewResolver, placeholders);

        this.placeholder = new RewardPreviewPlaceholder(this.previewResolver);
        this.dataExtension = new RewardPreviewDataExtension(this.previewResolver);
        this.api = new DefaultRewardPreviewAPI(this.previewResolver, this.previewService);

        this.addComponent(settingsController);
    }

    public RewardPreviewPlaceholder getPlaceholder() {
        return this.placeholder;
    }

    public RewardPreviewDataExtension getDataExtension() {
        return this.dataExtension;
    }
}
