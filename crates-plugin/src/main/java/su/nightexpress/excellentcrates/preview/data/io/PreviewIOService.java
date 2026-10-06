package su.nightexpress.excellentcrates.preview.data.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.preview.Preview;
import su.nightexpress.excellentcrates.api.preview.PreviewProvider;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class PreviewIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreviewIOService.class);

    private final Path directory;

    public PreviewIOService(Path directory) {
        this.directory = directory;
    }

    public List<Preview> loadPreviews(PreviewProvider provider) {
        List<Preview> previews = new ArrayList<>();

        Identifier providerId = provider.getId();
        KeyDomain providerDomain = KeyDomain.of(providerId.value());

        Path providerDir = this.directory.resolve(providerId.value());
        if (!Files.exists(providerDir)) {
            this.writeDefaultConfig(provider, providerDomain, providerDir);
        }

        FileUtil.findYamlFiles(providerDir).forEach(file -> {
            Preview preview = this.loadPreview(provider, file, providerDomain);
            if (preview != null) {
                previews.add(preview);
            }
        });

        return previews;
    }

    public @Nullable Preview loadPreview(PreviewProvider provider, Path file, KeyDomain providerDomain) {
        String fileName = FileUtil.getNameWithoutExtension(file);
        if (!BukkitKeys.isValidValue(fileName)) {
            LOGGER.warn("Invalid file name '{}' for preview configuration file: {}", fileName, file);
            return null;
        }

        FileConfig config = FileConfig.load(file);
        AdaptedKey key = providerDomain.make(fileName);

        Preview openingConfig = provider.parseConfig(key, config);
        config.saveChanges();

        return openingConfig;
    }

    private void writeDefaultConfig(PreviewProvider provider, KeyDomain providerDomain, Path configsDir) {
        try {
            Files.createDirectories(configsDir);

            AdaptedKey defaultKey = providerDomain.make("default");
            String defaultFileName = defaultKey.value() + ".yml";
            Path defaultFilePath = configsDir.resolve(defaultFileName);

            FileConfig.load(defaultFilePath).edit(config -> {
                provider.writeDefaultConfig(config);
            });
        }
        catch (IOException exception) {
            LOGGER.error("Failed to create default configuration for provider: {}", provider.getId());
            LOGGER.error("Reason: ", exception);
        }
    }
}
