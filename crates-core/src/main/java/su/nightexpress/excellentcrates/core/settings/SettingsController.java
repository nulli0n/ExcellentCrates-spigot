package su.nightexpress.excellentcrates.core.settings;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.PluginComponent;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class SettingsController<T> implements PluginComponent {

    private final Path                path;
    private final SettingsLoader<T>   loader;
    private final SettingsProvider<T> provider;

    public SettingsController(Path path, SettingsLoader<T> loader, SettingsProvider<T> provider) {
        this.path = path;
        this.loader = loader;
        this.provider = provider;
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
        this.loadSettings();
    }

    @Override
    public boolean isRunning() {
        return true;
    }
}
