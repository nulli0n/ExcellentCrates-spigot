package su.nightexpress.excellentcrates.config;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.configuration.codec.ConfigCodec;
import su.nightexpress.nightcore.configuration.property.ConfigProperty;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConfigOption<T> {

    private static final List<ConfigOption<?>> OPTIONS = new CopyOnWriteArrayList<>();

    private final ConfigProperty<T> property;

    private volatile T value;

    private ConfigOption(@NotNull ConfigProperty<T> property) {
        this.property = property;
        this.value = property.getDefaultValue();
    }

    @NotNull
    public static <T> ConfigOption<T> create(@NotNull ConfigCodec<T> codec, @NotNull String path, @NotNull T defaultValue, @NotNull String... description) {
        ConfigOption<T> option = new ConfigOption<>(ConfigProperty.of(codec, path, defaultValue, description));
        OPTIONS.add(option);
        return option;
    }

    public static void loadAll(@NotNull FileConfig config) {
        OPTIONS.forEach(option -> option.load(config));
    }

    public void load(@NotNull FileConfig config) {
        this.value = config.getOrSet(this.property);
    }

    @NotNull
    public T get() {
        return this.value;
    }

    @NotNull
    public String getPath() {
        return this.property.getRelativePath();
    }
}
