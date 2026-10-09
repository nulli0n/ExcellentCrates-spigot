package su.nightexpress.excellentcrates.keys.cost;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.registry.KeyRegistry;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.keys.balance.KeyBalanceService;
import su.nightexpress.excellentcrates.keys.cost.evaluator.KeyCostDisplayProvider;
import su.nightexpress.excellentcrates.keys.cost.evaluator.KeyCostLogicProvider;
import su.nightexpress.excellentcrates.keys.cost.evaluator.KeyCostType;
import su.nightexpress.excellentcrates.keys.cost.lang.KeyCostLang;
import su.nightexpress.excellentcrates.keys.cost.settings.KeyCostSettings;
import su.nightexpress.excellentcrates.keys.item.KeyItemFactory;

@NullMarked
public class KeyCostBootstrapContext extends NamedBootstrapContext {

    private static final Identifier KEY_ID = new Identifier("keys.cost");
    private static final String     NAME   = "Keys Cost";

    private static final String SETTINGS_FILE_NAME = "keys.cost.yml";

    private final KeyCostType costType;

    public KeyCostBootstrapContext(CratesPlugin plugin,
                                   KeyRegistry registry,
                                   KeyItemFactory itemFactory,
                                   KeyBalanceService balanceService) {
        super(KEY_ID, NAME);

        plugin.injectLang(KeyCostLang.class);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<KeyCostSettings> settings = new SettingsProvider<>(KeyCostSettings.defaultSettings());

        KeyCostDisplayProvider display = new KeyCostDisplayProvider(settings, registry, itemFactory);
        KeyCostLogicProvider logic = new KeyCostLogicProvider(registry, balanceService);
        this.costType = new KeyCostType(logic, display);

        this.addComponent(new SettingsController<>(settingsPath, KeyCostSettings::loadFrom, settings));
    }

    public KeyCostType getCostType() {
        return this.costType;
    }
}
