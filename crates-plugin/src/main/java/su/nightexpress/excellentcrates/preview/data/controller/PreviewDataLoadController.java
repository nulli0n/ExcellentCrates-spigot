package su.nightexpress.excellentcrates.preview.data.controller;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;
import su.nightexpress.excellentcrates.api.preview.PreviewRegistry;
import su.nightexpress.excellentcrates.preview.data.PreviewDataService;

@NullMarked
public class PreviewDataLoadController extends BasePluginComponent {

    private final PreviewRegistry    registry;
    private final PreviewDataService dataService;

    public PreviewDataLoadController(PreviewRegistry registry, PreviewDataService dataService) {
        super();
        this.registry = registry;
        this.dataService = dataService;
    }

    @Override
    protected void onReload() {
        this.registry.clearPreviews(); // Clear previews only, so providers can load them again.
        this.dataService.loadPreviews();
    }

    @Override
    protected void onShutdown() {
        this.registry.clear();
    }

    @Override
    protected void onStart() {
        this.dataService.loadPreviews();
    }
}
