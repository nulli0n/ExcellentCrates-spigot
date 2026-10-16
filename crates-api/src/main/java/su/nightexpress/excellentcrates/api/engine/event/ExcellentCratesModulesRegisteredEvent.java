package su.nightexpress.excellentcrates.api.engine.event;

import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
/**
 * Event fired after all modules have been registered (1st bootstrap phase completed).
 * At this point, you can access API of the registered modules from ServiceRegistry.
 */
public class ExcellentCratesModulesRegisteredEvent extends ExcellentCratesBootstrapEvent {

    public static final HandlerList HANDLERS = new HandlerList();

    public ExcellentCratesModulesRegisteredEvent(ComponentBundle core,
                                                 ServiceRegistry services,
                                                 CoreDependencies dependencies) {
        super(core, services, dependencies);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
