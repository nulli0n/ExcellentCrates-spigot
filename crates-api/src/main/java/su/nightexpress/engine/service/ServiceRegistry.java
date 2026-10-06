package su.nightexpress.engine.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ServiceRegistry {

    private final Map<ServiceKey<?>, PluginAPI> services = new HashMap<>();

    public <T extends PluginAPI> void register(ServiceKey<T> key, T implementation) {
        services.put(key, implementation);
    }

    public <T extends PluginAPI> @Nullable T get(ServiceKey<T> key) {
        return this.lookup(key).orElse(null);
    }

    public <T extends PluginAPI> T require(ServiceKey<T> key) {
        return this.lookup(key).orElseThrow(() -> new IllegalStateException("Service not found: " + key));
    }

    public <T extends PluginAPI> Optional<T> lookup(ServiceKey<T> key) {
        return Optional.ofNullable(key.type().cast(services.get(key)));
    }
}