package su.nightexpress.excellentcrates.opening.inventory.script;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ParameterResult {

    private final Map<Parameter<?>, Object> params;

    public ParameterResult() {
        this.params = new HashMap<>();
    }

    @NotNull
    public Map<Parameter<?>, Object> getParams() {
        return params;
    }

    public <V> void add(@NotNull Parameter<V> parameter, @NotNull Object value) {
        this.params.put(parameter, value);
    }

    @SuppressWarnings("unchecked")
    public <V> V get(@NotNull Parameter<V> parameter, V def) {
        V param = (V) this.params.get(parameter);
        return param == null ? def : param;
    }
}
