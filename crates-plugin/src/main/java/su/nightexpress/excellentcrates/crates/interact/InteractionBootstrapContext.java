package su.nightexpress.excellentcrates.crates.interact;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.IdentifiableRegistry;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.interact.action.InteractAction;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.interact.cooldown.InteractCooldownService;
import su.nightexpress.excellentcrates.crates.interact.cooldown.InteractCooldownSessionController;
import su.nightexpress.excellentcrates.crates.interact.cooldown.InteractCooldownTracker;
import su.nightexpress.excellentcrates.crates.interact.lang.InteractionLang;
import su.nightexpress.excellentcrates.crates.interact.settings.InteractSettings;

@NullMarked
public final class InteractionBootstrapContext extends NamedBootstrapContext {

    private static final Identifier BUNDLE_ID   = new Identifier("crates.interaction");
    private static final String     BUNDLE_NAME = "Interaction";

    private static final String SETTINGS_FILE_NAME = "crates.interaction.yml";

    public final IdentifiableRegistry<InteractAction> actions;

    public CrateInteractionService interactionService;
    public DefaultInteractionAPI   api;

    public InteractionBootstrapContext(CratesPlugin plugin) {
        super(BUNDLE_ID, BUNDLE_NAME);
        this.actions = new IdentifiableRegistry<>();

        plugin.injectLang(InteractionLang.class);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<InteractSettings> settings = new SettingsProvider<>(InteractSettings.defaults());

        InteractCooldownTracker cooldownTracker = new InteractCooldownTracker();
        InteractCooldownService cooldownService = new InteractCooldownService(settings, cooldownTracker);

        this.interactionService = new CrateInteractionService(this.actions, cooldownService);
        this.api = new DefaultInteractionAPI(this.actions, this.interactionService);

        this.addComponent(new SettingsController<>(settingsPath, InteractSettings::loadFrom, settings));
        this.addComponent(new InteractCooldownSessionController(plugin, cooldownService));
    }
}
