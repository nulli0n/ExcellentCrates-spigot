package su.nightexpress.engine.entity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public class MutableEntityComponentContainer<T extends EntityComponent> implements MutableEntityComponents<T> {

    private final Map<Identifier, T> components;

    public MutableEntityComponentContainer(Map<Identifier, T> components) {
        this.components = new HashMap<>(components);
    }

    @Override
    public Collection<T> values() {
        return this.components.values();
    }

    @Override
    public <E extends T> boolean hasComponent(EntityComponentKey<E> type) {
        return this.components.containsKey(type.getId());
    }

    @Override
    public <E extends T> @Nullable E getComponentOrNull(EntityComponentKey<E> type) {
        return this.getComponent(type).orElse(null);
    }

    @Override
    public <E extends T> Optional<E> getComponent(EntityComponentKey<E> type) {
        T component = this.components.get(type.getId());
        return component == null ? Optional.empty() : Optional.of(type.getType().cast(component));
    }

    @Override
    public <E extends T> E getComponentStrict(EntityComponentKey<E> type) {
        return this.getComponent(type)
            .orElseThrow(() -> new IllegalArgumentException("Missing required component: '" + type.getId() + "'"));
    }

    @Override
    public <E extends T> void putComponent(EntityComponentKey<E> key, E component) {
        this.components.put(key.getId(), component);
    }

    @Override
    public <E extends T> void removeComponent(EntityComponentKey<E> key) {
        this.components.remove(key.getId());
    }
}
