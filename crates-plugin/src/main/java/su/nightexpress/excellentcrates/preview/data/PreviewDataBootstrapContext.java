package su.nightexpress.excellentcrates.preview.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.data.controller.PreviewDataLoadController;
import su.nightexpress.excellentcrates.preview.data.io.PreviewIOService;

@NullMarked
public class PreviewDataBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("preview.data");
    private static final String     NAME = "IO/Data";

    private static final String PREVIEW_DATA_DIRECTORY = "preview";

    public final PreviewIOService   ioService;
    public final PreviewDataService dataService;

    public PreviewDataBootstrapContext(CratesPlugin plugin, PreviewRegistry registry) {
        super(ID, NAME);

        Path previewDir = plugin.objectsPath().resolve(PREVIEW_DATA_DIRECTORY);

        this.ioService = new PreviewIOService(previewDir);
        this.dataService = new PreviewDataService(registry, ioService);

        this.addComponent(new PreviewDataLoadController(registry, dataService));
    }
}
