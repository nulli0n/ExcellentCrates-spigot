package su.nightexpress.excellentcrates.opening.inventory.script;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class ScriptAction {

    private final String                                             name;
    private final     Set<Parameter<?>>                             parameters;
    private final     BiConsumer<InventoryOpening, ParameterResult> executor;

    public ScriptAction(@NotNull String name,
                        @NotNull BiConsumer<InventoryOpening, ParameterResult> executor,
                        Parameter<?>...                       parameters) {
        this.name = name.toLowerCase();
        this.parameters = new HashSet<>(Arrays.asList(parameters));
        this.executor = executor;
        this.getParameters().add(Parameters.DELAY);
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Set<Parameter<?>> getParameters() {
        return parameters;
    }

    @NotNull
    public BiConsumer<InventoryOpening, ParameterResult> getExecutor() {
        return executor;
    }

    public void run(@NotNull InventoryOpening opening, @NotNull ParameterResult parameterResult) {
        this.getExecutor().accept(opening, parameterResult);
    }
}
