package su.nightexpress.excellentcrates.api;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderAPIResolver;
import su.nightexpress.nightcore.NightCorePlugin;
import su.nightexpress.nightcore.bridge.key.KeyDomain;

@NullMarked
public interface CratesPlugin extends NightCorePlugin {

    void addPlaceholderAPIResolver(PlaceholderAPIResolver resolver);

    KeyDomain keyDomain();

    Path configPath();

    Path objectsPath();

    Path menuPath();

    Path logsPath();
}
