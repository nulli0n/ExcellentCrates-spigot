package su.nightexpress.excellentcrates.animation.data.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.animation.AnimationProfile;
import su.nightexpress.excellentcrates.api.animation.AnimationProvider;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.FileUtil;
import su.nightexpress.nightcore.util.LowerCase;

@NullMarked
public class AnimationIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnimationIOService.class);

    private final Path animationsDir;

    public AnimationIOService(Path animationsDir) {
        this.animationsDir = animationsDir;
    }

    public List<AnimationProfile> loadProfiles(AnimationProvider<?> provider) {
        Identifier providerId = provider.getId();
        KeyDomain providerDomain = KeyDomain.of(providerId.value());

        Path configsDir = this.animationsDir.resolve(providerId.value());
        if (!Files.exists(configsDir)) {
            this.writeDefaultConfig(provider, providerDomain, configsDir);
        }

        List<AnimationProfile> configs = new ArrayList<>();

        FileUtil.findYamlFiles(configsDir).forEach(file -> {
            String fileName = LowerCase.internal(FileUtil.getNameWithoutExtension(file));
            if (!BukkitKeys.isValidValue(fileName)) {
                LOGGER.warn("Invalid file name '{}' for animation configuration file: {}", fileName, file);
                return;
            }

            FileConfig config = FileConfig.load(file);
            AdaptedKey key = providerDomain.make(fileName);

            AnimationProfile profile = provider.readProfile(key, config);
            configs.add(profile);
            config.saveChanges();
        });

        return configs;
    }

    private void writeDefaultConfig(AnimationProvider<?> provider, KeyDomain providerDomain, Path configsDir) {
        try {
            Files.createDirectories(configsDir);

            AdaptedKey defaultKey = providerDomain.make("default");
            String defaultFileName = FileConfig.withExtension(defaultKey.value());
            Path defaultFilePath = configsDir.resolve(defaultFileName);

            FileConfig.load(defaultFilePath).edit(config -> {
                provider.writeDefaultProfile(defaultKey, config);
            });
        }
        catch (IOException exception) {
            LOGGER.error("Failed to create default configuration for provider: {}", provider.getId());
            LOGGER.error("Reason: ", exception);
        }
    }
}
