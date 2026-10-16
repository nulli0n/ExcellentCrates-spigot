package su.nightexpress.engine.component;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.ServiceRegistry;

@NullMarked
public interface ModuleBootstrap<D extends EngineDependencies> {

    /**
     * Phase 1 - Initialization
     */
    void onRegister(ServiceRegistry services, @NonNull D dependencies);

    /**
     * Phase 2 - Resolution
     */
    PluginComponent onResolve(ServiceRegistry services, @NonNull D dependencies);
}