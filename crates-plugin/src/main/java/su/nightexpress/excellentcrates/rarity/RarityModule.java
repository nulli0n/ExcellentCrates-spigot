package su.nightexpress.excellentcrates.rarity;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class RarityModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("rarity");
    private static final String     NAME = "Rarity";

    public RarityModule() {
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
