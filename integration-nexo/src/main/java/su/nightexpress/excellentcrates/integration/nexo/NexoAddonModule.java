package su.nightexpress.excellentcrates.integration.nexo;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class NexoAddonModule extends ModuleComponent {

    public NexoAddonModule(Identifier id) {
        super(id, "NexoAddon");
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {

    }
}
