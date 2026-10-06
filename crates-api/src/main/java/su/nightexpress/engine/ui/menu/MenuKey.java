package su.nightexpress.engine.ui.menu;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record MenuKey<T>(Identifier id) {

    public static <T> MenuKey<T> of(String id) {
        return new MenuKey<>(new Identifier(id));
    }
}
