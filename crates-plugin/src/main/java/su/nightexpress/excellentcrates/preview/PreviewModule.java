package su.nightexpress.excellentcrates.preview;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class PreviewModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("preview");
    private static final String     NAME = "Preview";

    public PreviewModule() {
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
