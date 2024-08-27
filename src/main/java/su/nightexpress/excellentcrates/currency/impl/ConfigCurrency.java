package su.nightexpress.excellentcrates.currency.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.currency.Currency;
import su.nightexpress.excellentcrates.api.currency.CurrencyHandler;
import su.nightexpress.nightcore.config.ConfigValue;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.StringUtil;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

public class ConfigCurrency implements Currency {

    private final boolean enabled;
    private final String id;
    private final String name;
    private final String format;
    private final CurrencyHandler handler;
    private final PlaceholderMap placeholderMap;

    public ConfigCurrency(boolean enabled, @NotNull String id, @NotNull String name, @NotNull String format, @NotNull CurrencyHandler handler) {
        this.enabled = enabled;
        this.id = StringUtil.lowerCaseUnderscore(id);
        this.name = name;
        this.format = format;
        this.handler = handler;
        this.placeholderMap = Placeholders.forCurrency(this);
    }

    @NotNull
    public static ConfigCurrency read(@NotNull FileConfig config, @NotNull String path, @NotNull String id, @NotNull CurrencyHandler handler) {
        boolean enabled = ConfigValue.create(path + ".Enabled", true).read(config);
        String name = ConfigValue.create(path + ".Name", handler.getDefaultName()).read(config);
        String format = ConfigValue.create(path + ".Format", handler.getDefaultFormat()).read(config);

        return new ConfigCurrency(enabled, id, name, format, handler);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Name", this.getName());
        config.set(path + ".Format", this.getFormat());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @NotNull
    @Override
    public CurrencyHandler getHandler() {
        return handler;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @NotNull
    @Override
    public String getId() {
        return id;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @NotNull
    @Override
    public String getFormat() {
        return format;
    }
}
