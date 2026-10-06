package su.nightexpress.excellentcrates.keys;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class KeysModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("keys");
    private static final String     NAME = "Keys";

    public KeysModule() {
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
