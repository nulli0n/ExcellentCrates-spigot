package su.nightexpress.excellentcrates.api.engine.event;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
public abstract class ExcellentCratesBootstrapEvent extends Event {

    protected final PluginComponent  core;
    protected final ServiceRegistry  services;
    protected final CoreDependencies dependencies;

    public ExcellentCratesBootstrapEvent(PluginComponent core,
                                         ServiceRegistry services,
                                         CoreDependencies dependencies) {
        super();
        this.core = core;
        this.services = services;
        this.dependencies = dependencies;
    }

    public PluginComponent getCore() {
        return core;
    }

    /**
     * Gets the service registry associated with this bootstrap event.
     * It is illegal to cache the service registry reference.
     *
     * @return the service registry
     */
    public ServiceRegistry getServices() {
        return services;
    }

    /**
     * Gets the core dependencies associated with this bootstrap event.
     * It is illegal to cache the dependencies reference.
     *
     * @return the core dependencies
     */
    public CoreDependencies getDependencies() {
        return dependencies;
    }
}
