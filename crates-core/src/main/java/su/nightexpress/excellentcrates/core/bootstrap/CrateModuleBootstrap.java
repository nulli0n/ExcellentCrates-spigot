package su.nightexpress.excellentcrates.core.bootstrap;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
public abstract class CrateModuleBootstrap extends BaseModuleBootstrap<CoreDependencies> {

    protected CrateModuleBootstrap() {
        super();
    }

}
