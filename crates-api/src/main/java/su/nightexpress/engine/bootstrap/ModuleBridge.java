package su.nightexpress.engine.bootstrap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.engine.service.ServiceKey;
import su.nightexpress.engine.service.ServiceRegistry;

@NullMarked
public class ModuleBridge {

    private final List<Consumer<ServiceRegistry>> hooks = new ArrayList<>();

    public <T extends PluginAPI> void onAvailable(ServiceKey<T> key, Consumer<T> action) {
        this.hooks.add(registry -> registry.lookup(key).ifPresent(action));
    }

    public <T extends PluginAPI> void requireAvailable(ServiceKey<T> key, Consumer<T> action) {
        this.hooks.add(registry -> action.accept(registry.require(key)));
    }

    public void executeHooks(ServiceRegistry registry) {
        while (!this.hooks.isEmpty()) {
            // Copy the current snapshot of hooks to avoid ConcurrentModificationException
            List<Consumer<ServiceRegistry>> currentBatch = new ArrayList<>(this.hooks);

            // Clear the original list. Any nested this.bridge.onAvailable calls will go into the empty list
            // and will be processed in the next iteration of the while loop.
            this.hooks.clear();

            // Execute the current batch of hooks
            for (Consumer<ServiceRegistry> hook : currentBatch) {
                hook.accept(registry);
            }
        }
    }
}