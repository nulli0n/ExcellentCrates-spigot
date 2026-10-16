package su.nightexpress.excellentcrates.api.engine.event;

import org.bukkit.event.HandlerList;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
/**
 * Event fired after all modules have been resolved.
 */
public class ExcellentCratesModulesResolvedEvent extends ExcellentCratesBootstrapEvent {

    public static final HandlerList HANDLERS = new HandlerList();

    public ExcellentCratesModulesResolvedEvent(PluginComponent core,
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
