package su.nightexpress.engine.placeholder;

import java.util.function.Consumer;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@FunctionalInterface
@NullMarked
public interface PlaceholderApplier extends Consumer<PlaceholderContext.Builder> {

    static PlaceholderApplier empty() {
        return builder -> {
        };
    }
}