package su.nightexpress.excellentcrates.crates.batch;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.core.settings.SettingsController;
import su.nightexpress.excellentcrates.core.settings.SettingsProvider;
import su.nightexpress.excellentcrates.crates.batch.pipeline.BatchInitializePipelineStage;
import su.nightexpress.excellentcrates.crates.batch.pipeline.BatchSelectionPipelineStage;
import su.nightexpress.excellentcrates.crates.batch.settings.BatchSettings;
import su.nightexpress.excellentcrates.crates.batch.ui.BatchUIController;
import su.nightexpress.excellentcrates.crates.batch.ui.BatchUIService;
import su.nightexpress.excellentcrates.crates.batch.ui.controller.BatchUIMenuRegistrar;

@NullMarked
public class BatchBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.batch");
    private static final String     NAME = "Batch";

    private static final String SETTINGS_FILE_NAME = "crates.batch.yml";

    private final BatchInitializePipelineStage initializePipelineStage;
    private final BatchSelectionPipelineStage  selectionPipelineStage;

    public BatchBootstrapContext(CratesPlugin plugin,
                                 CoreUIService coreUI,
                                 CrateMessageDispatcher dispatcher,
                                 CrateRegistry crates) {
        super(ID, NAME);

        Path settingsPath = plugin.configPath().resolve(SETTINGS_FILE_NAME);
        SettingsProvider<BatchSettings> settings = new SettingsProvider<>(BatchSettings.defaultSettings());

        BatchUIService uiService = new BatchUIService(coreUI);
        BatchUIController uiController = new BatchUIController(uiService, dispatcher);

        this.initializePipelineStage = new BatchInitializePipelineStage(settings);
        this.selectionPipelineStage = new BatchSelectionPipelineStage(uiController, settings);

        this.addComponent(new SettingsController<>(settingsPath, BatchSettings::loadFrom, settings));
        this.addComponent(new BatchUIMenuRegistrar(plugin, coreUI));
    }

    public BatchInitializePipelineStage getInitializePipelineStage() {
        return this.initializePipelineStage;
    }

    public BatchSelectionPipelineStage getSelectionPipelineStage() {
        return this.selectionPipelineStage;
    }
}
