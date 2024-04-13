package su.nightexpress.excellentcrates.opening.inventory.script;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.spinner.SpinMode;
import su.nightexpress.nightcore.util.Colorizer;
import su.nightexpress.nightcore.util.NumberUtil;
import su.nightexpress.nightcore.util.StringUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class Parameters {

    private static final Map<String, Parameter<?>> REGISTRY = new HashMap<>();

    public static final Parameter<Integer> DELAY = asInt("delay");

    public static final Parameter<String> ID = asString("id");

    public static final Parameter<String> NAME = asString("name");

    public static final Parameter<SpinMode> SPIN_MODE = register("mode", str -> StringUtil.getEnum(str, SpinMode.class).orElse(SpinMode.INDEPENDENT));

    public static final Parameter<Double> CHANCE = asDouble("chance");

    public static final Parameter<int[]> SLOTS = register("slots", NumberUtil::getIntArray);

    public static final Parameter<Integer> AMOUNT = asInt("amount");

    public static final Parameter<Double> DOUBLE_VALUE = asDouble("value");

    @NotNull
    public static Optional<Parameter<?>> getByName(@NotNull String name) {
        return Optional.ofNullable(REGISTRY.get(name.toLowerCase()));
    }

    @NotNull
    public static Collection<Parameter<?>> getParameters() {
        return REGISTRY.values();
    }

    @NotNull
    public static <V> Parameter<V> register(@NotNull String name, @NotNull Function<String, V> parser) {
        Parameter<V> parameter = new Parameter<>(name, parser);
        REGISTRY.put(parameter.getName(), parameter);
        return parameter;
    }

    @NotNull
    public static Parameter<Integer> asInt(@NotNull String name) {
        Function<String, Integer> parser = str -> NumberUtil.getAnyInteger(str, 0);
        return register(name, parser);
    }

    @NotNull
    public static Parameter<Double> asDouble(@NotNull String name) {
        Function<String, Double> parser = str -> NumberUtil.getAnyDouble(str, 0);
        return register(name, parser);
    }

    @NotNull
    public static Parameter<String> asString(@NotNull String name) {
        Function<String, String> parser = Colorizer::strip;
        return register(name, parser);
    }
}
