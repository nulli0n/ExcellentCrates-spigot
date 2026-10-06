package su.nightexpress.excellentcrates.effect.data.io;

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
import su.nightexpress.excellentcrates.api.effect.EffectModel;
import su.nightexpress.excellentcrates.api.effect.EffectModelSettings;
import su.nightexpress.excellentcrates.api.effect.EffectProfile;
import su.nightexpress.nightcore.bridge.BukkitKeys;
import su.nightexpress.nightcore.bridge.key.AdaptedKey;
import su.nightexpress.nightcore.bridge.key.KeyDomain;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class EffectIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EffectIOService.class);

    private final Path directory;

    public EffectIOService(Path directory) {
        this.directory = directory;
    }

    public <T extends EffectModelSettings> List<EffectProfile<T>> loadProfiles(EffectModel<T> model) {
        List<EffectProfile<T>> profiles = new ArrayList<>();

        Identifier modelId = model.getId();
        String modelName = modelId.value();
        if (!BukkitKeys.isValidNamespace(modelName)) {
            LOGGER.warn("Invalid model namespace '{}'", modelName);
            return profiles;
        }

        KeyDomain modelDomain = KeyDomain.of(modelName);

        Path modelDir = this.directory.resolve(modelName);
        if (!Files.exists(modelDir)) {
            this.writeDefaultConfig(model, modelDomain, modelDir);
        }

        FileUtil.findYamlFiles(modelDir).forEach(file -> {
            EffectProfile<T> profile = this.loadProfile(model, file, modelDomain);
            if (profile != null) {
                profiles.add(profile);
            }
        });

        return profiles;
    }

    public @Nullable <T extends EffectModelSettings> EffectProfile<T> loadProfile(EffectModel<T> model, Path file,
                                                                                  KeyDomain modelDomain) {
        String fileName = FileUtil.getNameWithoutExtension(file);
        if (!BukkitKeys.isValidValue(fileName)) {
            LOGGER.warn("Invalid file name '{}' for effect profile file: {}", fileName, file);
            return null;
        }

        FileConfig config = FileConfig.load(file);
        AdaptedKey key = modelDomain.make(fileName);

        EffectProfile<T> profile = model.loadProfile(key, config);
        config.saveChanges();

        return profile;
    }

    private void writeDefaultConfig(EffectModel<?> model, KeyDomain modelDomain, Path profilesDir) {
        try {
            Files.createDirectories(profilesDir);

            AdaptedKey defaultKey = modelDomain.make("default");
            String defaultFileName = defaultKey.value() + ".yml";
            Path defaultFilePath = profilesDir.resolve(defaultFileName);

            FileConfig.load(defaultFilePath).edit(config -> {
                model.writeDefaultProfile(config);
            });
        }
        catch (IOException exception) {
            LOGGER.error("Failed to create default effect profile for model: {}", model.getId());
            LOGGER.error("Reason: ", exception);
        }
    }
}
