package su.nightexpress.engine.entity;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MutableEntityComponents<T extends EntityComponent> extends EntityComponents<T> {

    <E extends T> void putComponent(EntityComponentKey<E> key, E component);

    <E extends T> void removeComponent(EntityComponentKey<E> key);
}
