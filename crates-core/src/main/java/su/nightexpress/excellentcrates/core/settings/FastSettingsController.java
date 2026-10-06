package su.nightexpress.excellentcrates.core.settings;

import java.nio.file.Path;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.nightcore.config.FileConfig;

public class FastSettingsController<T> implements PluginComponent {

    private final Path                path;
    private final SettingsLoader<T>   loader;
    private final SettingsProvider<T> provider;

    public FastSettingsController(Path path, SettingsLoader<T> loader, SettingsProvider<T> provider) {
        this.path = path;
        this.loader = loader;
        this.provider = provider;
    }

    public static <T> FastSettingsController<T> createAndLoad(Path path,
                                                              SettingsLoader<T> loader,
                                                              SettingsProvider<T> provider) {
        FastSettingsController<T> controller = new FastSettingsController<>(path, loader, provider);
        controller.loadSettings();
        return controller;
    }

    public void loadSettings() {
        FileConfig.load(this.path).edit(config -> {
            T newSettings = this.loader.loadFrom(config);
            this.provider.update(newSettings);
        });
    }

    @Override
    public void reload() {
        this.loadSettings();
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void start() {

    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
