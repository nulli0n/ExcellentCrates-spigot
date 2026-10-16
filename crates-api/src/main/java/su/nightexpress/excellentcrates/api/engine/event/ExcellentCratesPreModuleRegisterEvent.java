package su.nightexpress.excellentcrates.api.engine.event;

import java.util.List;

import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.ModuleBootstrap;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
/**
 * Event fired before module registration in ExcellentCrates.
 * Allows inspection or modification of module bootstraps before they are registered.
 * At this point, no modules have been registered in ServiceRegistry yet.
 */
public class ExcellentCratesPreModuleRegisterEvent extends ExcellentCratesBootstrapEvent {

    public static final HandlerList HANDLERS = new HandlerList();

    private final List<ModuleBootstrap<CoreDependencies>> moduleBootstraps;

    public ExcellentCratesPreModuleRegisterEvent(ComponentBundle core,
                                                 ServiceRegistry services,
                                                 CoreDependencies dependencies,
                                                 List<ModuleBootstrap<CoreDependencies>> moduleBootstraps) {
        super(core, services, dependencies);
        this.moduleBootstraps = moduleBootstraps;
    }

    /**
     * Gets the list of module bootstraps to be registered.
     * You can use this list to inspect or modify the modules before they are registered.
     *
     * @return the list of module bootstraps
     */
    public List<ModuleBootstrap<CoreDependencies>> getModuleBootstraps() {
        return this.moduleBootstraps;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
