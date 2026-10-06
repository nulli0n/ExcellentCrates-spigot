package su.nightexpress.engine.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ComponentBundle implements PluginComponent {

    private final List<PluginComponent>          components;
    private final Map<Class<?>, PluginComponent> mirrorCache;

    private boolean isRunning;

    public ComponentBundle() {
        this.components = new ArrayList<>();
        this.mirrorCache = new HashMap<>();
    }

    public boolean isRegistered(PluginComponent component) {
        return this.components.contains(component);
    }

    public void addComponent(PluginComponent component) {
        if (this.components.contains(component)) {
            throw new IllegalArgumentException("Component is already registered!");
        }

        this.components.add(component);

        this.mirrorCache.put(component.getClass(), component);
    }

    /**
     * Retrieves a registered component by its exact class or implemented interface.
     * The generic bound is <T> rather than <T extends LifecycleComponent> to allow
     * looking up pure API interfaces that don't expose lifecycle methods.
     */
    public <T> T getComponent(Class<T> componentType) {
        // Try an exact match first
        PluginComponent match = this.mirrorCache.get(componentType);

        // Fallback to polymorphic lookup if not yet cached
        if (match == null) {
            for (PluginComponent component : this.components) {
                if (componentType.isInstance(component)) {
                    match = component;

                    // Lazily populate the mirror map with the interface/superclass.
                    this.mirrorCache.put(componentType, match);
                    break;
                }
            }
        }

        if (match == null) {
            throw new IllegalArgumentException("Component " + componentType.getSimpleName() + " is not loaded!");
        }

        return componentType.cast(match);
    }

    public <T> Optional<T> component(Class<T> componentType) {
        try {
            return Optional.of(this.getComponent(componentType));
        }
        catch (IllegalArgumentException exception) {
            return Optional.empty();
        }
    }

    public List<PluginComponent> getComponents() {
        return Collections.unmodifiableList(this.components);
    }

    @Override
    public void start() {
        if (this.isRunning) return;

        this.components.forEach(PluginComponent::start);
        this.isRunning = true;
    }

    @Override
    public void shutdown() {
        if (!this.isRunning) return;

        List<PluginComponent> list = this.components;
        for (int index = list.size() - 1; index >= 0; index--) {
            list.get(index).shutdown();
        }

        this.isRunning = false;
    }

    @Override
    public void reload() {
        this.components.forEach(PluginComponent::reload);
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }
}