package su.nightexpress.excellentcrates.crates;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class CratesModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("crates");
    private static final String     NAME = "Crates";

    public CratesModule() {
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
