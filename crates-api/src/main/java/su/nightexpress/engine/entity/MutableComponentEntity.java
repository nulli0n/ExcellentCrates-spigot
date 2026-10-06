package su.nightexpress.engine.entity;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface MutableComponentEntity<T extends EntityComponent> extends ComponentEntity<T> {

    MutableEntityComponents<T> getComponents();

    default <E extends T> void putComponent(EntityComponentKey<E> key, E component) {
        this.getComponents().putComponent(key, component);
    }

    default <E extends T> void removeComponent(EntityComponentKey<E> key) {
        this.getComponents().removeComponent(key);
    }

}
