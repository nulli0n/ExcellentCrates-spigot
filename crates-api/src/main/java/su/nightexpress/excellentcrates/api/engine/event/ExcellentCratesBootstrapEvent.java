package su.nightexpress.excellentcrates.api.engine.event;

import org.bukkit.event.Event;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.excellentcrates.api.CoreDependencies;

@NullMarked
public abstract class ExcellentCratesBootstrapEvent extends Event {

    protected final ComponentBundle  core;
    protected final ServiceRegistry  services;
    protected final CoreDependencies dependencies;

    public ExcellentCratesBootstrapEvent(ComponentBundle core,
                                         ServiceRegistry services,
                                         CoreDependencies dependencies) {
        super();
        this.core = core;
        this.services = services;
        this.dependencies = dependencies;
    }

    public ComponentBundle getCore() {
        return core;
    }

    /**
     * Gets the service registry associated with this bootstrap event.<br>
     * <b>It is illegal to cache the service registry reference.</b>
     *
     * @return the service registry
     */
    public ServiceRegistry getServices() {
        return services;
    }

    /**
     * Gets the core dependencies associated with this bootstrap event.<br>
     * <b>It is illegal to cache the dependencies reference.</b>
     *
     * @return the core dependencies
     */
    public CoreDependencies getDependencies() {
        return dependencies;
    }
}
