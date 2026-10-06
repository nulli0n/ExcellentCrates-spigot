package su.nightexpress.engine.entity;

import java.util.Collection;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface EntityComponents<T extends EntityComponent> {

    Collection<T> values();

    <E extends T> boolean hasComponent(EntityComponentKey<E> type);

    <E extends T> @Nullable E getComponentOrNull(EntityComponentKey<E> type);

    <E extends T> Optional<E> getComponent(EntityComponentKey<E> type);

    <E extends T> E getComponentStrict(EntityComponentKey<E> type);
}
