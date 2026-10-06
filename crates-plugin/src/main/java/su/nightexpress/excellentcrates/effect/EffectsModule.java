package su.nightexpress.excellentcrates.effect;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class EffectsModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("effects");
    private static final String     NAME = "Effects";

    public EffectsModule() {
        super(ID, NAME);
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
