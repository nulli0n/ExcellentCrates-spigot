package su.nightexpress.excellentcrates.core.settings;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
@FunctionalInterface
public interface SettingsLoader<T> {

    T loadFrom(FileConfig config);
}
