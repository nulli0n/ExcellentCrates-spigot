package su.nightexpress.engine.entity;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.engine.id.Identifier;

@NullMarked
public class EntityComponentKey<T> implements Identifiable {

    private final Identifier id;
    private final Class<T>   type;

    public EntityComponentKey(Identifier id, Class<T> type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Creates a new entity component key with the given identifier and type.
     *
     * @param id   The identifier of the component.
     * @param type The type of the component.
     * @param <T>  The type of the component.
     * @return A new entity component key.
     */
    public static <T> EntityComponentKey<T> of(String id, Class<T> type) {
        return of(new Identifier(id), type);
    }

    public static <T> EntityComponentKey<T> of(Identifier id, Class<T> type) {
        return new EntityComponentKey<>(id, type);
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public Class<T> getType() {
        return this.type;
    }
}
