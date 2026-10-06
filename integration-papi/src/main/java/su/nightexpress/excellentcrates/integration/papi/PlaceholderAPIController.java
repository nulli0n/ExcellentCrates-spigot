package su.nightexpress.excellentcrates.integration.papi;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BasePluginComponent;

@NullMarked
public class PlaceholderAPIController extends BasePluginComponent {

    private final PlaceholderAPIExpansion expansion;

    public PlaceholderAPIController(PlaceholderAPIExpansion expansion) {
        super();
        this.expansion = expansion;
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {
        this.expansion.unregister();
    }

    @Override
    protected void onStart() {
        this.expansion.register();
    }
}
