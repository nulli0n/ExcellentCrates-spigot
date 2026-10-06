package su.nightexpress.excellentcrates.integration.itemsadder;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class ItemsAdderAddonModule extends ModuleComponent {

    public ItemsAdderAddonModule(Identifier id) {
        super(id, "ItemsAdder");
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
