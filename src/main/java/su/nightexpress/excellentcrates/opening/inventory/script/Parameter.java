package su.nightexpress.excellentcrates.opening.inventory.script;

import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Parameter<V> {

    private final String              name;
    private final Function<String, V> parser;

    public Parameter(@NotNull String name, @NotNull Function<String, V> parser) {
        this.name = name.toLowerCase();
        this.parser = parser;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Function<String, V> getParser() {
        return parser;
    }

    @NotNull
    public V parse(@NotNull String str) {
        return this.getParser().apply(str);
    }
}
