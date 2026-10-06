package su.nightexpress.excellentcrates.core.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.settings.ReadOnlySettings;

@NullMarked
public class SettingsProvider<T> implements ReadOnlySettings<T> {

    private volatile T currentSettings;

    public SettingsProvider(T initialSettings) {
        this.currentSettings = initialSettings;
    }

    public void update(T newSettings) {
        this.currentSettings = newSettings;
    }

    @Override
    public T get() {
        return this.currentSettings;
    }
}
