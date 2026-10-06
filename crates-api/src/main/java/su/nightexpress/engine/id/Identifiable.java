package su.nightexpress.engine.id;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Identifiable {

    Identifier getId();

    default Identifier id() {
        return this.getId();
    }

    default String idString() {
        return this.id().value();
    }
}