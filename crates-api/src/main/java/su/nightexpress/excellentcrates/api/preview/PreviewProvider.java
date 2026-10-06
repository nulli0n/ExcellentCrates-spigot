package su.nightexpress.excellentcrates.api.preview;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifiable;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public interface PreviewProvider extends Identifiable {

    Preview parseConfig(AdaptedKey key, FileConfig config);

    void writeDefaultConfig(FileConfig config);
}
