package su.nightexpress.engine.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.ServiceRegistry;

@NullMarked
public interface ModuleBootstrap {

    /**
     * Phase 1 - Initialization
     */
    void onRegister(ServiceRegistry services, CoreDependencies dependencies);

    /**
     * Phase 2 - Resolution
     */
    PluginComponent onResolve(ServiceRegistry services, CoreDependencies dependencies);
}