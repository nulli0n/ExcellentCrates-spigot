package su.nightexpress.engine.bootstrap.context;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.ComponentHolder;
import su.nightexpress.engine.component.NamedComponentBundle;
import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public abstract class NamedBootstrapContext implements BootstrapContext {

    private final NamedComponentBundle bundle;

    public NamedBootstrapContext(Identifier id, String name) {
        this.bundle = new NamedComponentBundle(id, name);
    }

    protected void addComponent(PluginComponent component) {
        this.bundle.addComponent(component);
    }

    protected void addComponent(ComponentHolder holder) {
        this.bundle.addComponent(holder.getComponent());
    }

    @Override
    public PluginComponent getComponent() {
        return this.bundle;
    }
}
