package su.nightexpress.engine.component;

import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.ModuleBridge;
import su.nightexpress.engine.service.ServiceRegistry;

@NullMarked
public abstract class BaseModuleBootstrap implements ModuleBootstrap {

    private final List<PluginComponent> bootComponents;
    protected final ModuleBridge        bridge;

    protected BaseModuleBootstrap() {
        this.bootComponents = new ArrayList<>();
        this.bridge = new ModuleBridge();
    }

    protected final void registerComponent(ComponentHolder holder) {
        this.registerComponent(holder.getComponent());
    }

    protected final void registerComponent(PluginComponent component) {
        this.bootComponents.add(component);
    }

    protected final void registerComponents(List<PluginComponent> components) {
        this.bootComponents.addAll(components);
    }

    @Override
    public final ComponentBundle onResolve(ServiceRegistry services, CoreDependencies dependencies) {

        this.bridge.executeHooks(services);

        ComponentBundle bundle = this.buildComponent(services, dependencies);

        this.bootComponents.forEach(bundle::addComponent);
        this.bootComponents.clear();

        return bundle;
    }

    protected abstract ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies);
}
