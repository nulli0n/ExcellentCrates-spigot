package su.nightexpress.excellentcrates.animation;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class AnimationModule extends ModuleComponent {

    private static final Identifier ID   = new Identifier("animations");
    private static final String     NAME = "Animations";

    public AnimationModule() {
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
