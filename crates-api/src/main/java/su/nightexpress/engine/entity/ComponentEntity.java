package su.nightexpress.engine.entity;

import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface ComponentEntity<T extends EntityComponent> {

    EntityComponents<T> getComponents();

    default <E extends T> boolean hasComponent(EntityComponentKey<E> type) {
        return this.getComponents().hasComponent(type);
    }

    default <E extends T> E getComponentStrict(EntityComponentKey<E> type) {
        return this.getComponents().getComponentStrict(type);
    }

    default <E extends T> @Nullable E getComponentOrNull(EntityComponentKey<E> type) {
        return this.getComponents().getComponentOrNull(type);
    }

    default <E extends T> Optional<E> getComponent(EntityComponentKey<E> type) {
        return this.getComponents().getComponent(type);
    }
}
