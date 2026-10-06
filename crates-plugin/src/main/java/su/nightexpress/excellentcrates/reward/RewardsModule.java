package su.nightexpress.excellentcrates.reward;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ModuleComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class RewardsModule extends ModuleComponent {

    private static final Identifier MODULE_ID   = new Identifier("rewards");
    private static final String     MODULE_NAME = "Rewards";

    public RewardsModule() {
        super(MODULE_ID, MODULE_NAME);
    }

    @Override
    protected void onReload() {

    }

    @Override
    protected void onShutdown() {

    }

    @Override
    protected void onStart() {
        // Nothing yet
    }
}
