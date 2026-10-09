package su.nightexpress.excellentcrates.reward.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.id.IdentifierParser;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardBase;
import su.nightexpress.excellentcrates.api.reward.data.model.RewardPreview;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardBase;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardBuilder;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardPreview;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.exception.ModelLoadException;
import su.nightexpress.nightcore.util.FileUtil;

@NullMarked
public class RewardIOService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardIOService.class);

    private final Path                              rewardsDir;
    private final TinyRegistry<RewardDataExtension> extensions;

    public RewardIOService(Path rewardsDir, TinyRegistry<RewardDataExtension> extensions) {
        this.rewardsDir = rewardsDir;
        this.extensions = extensions;
    }

    public Path getRewardFile(Reward reward) {
        String crateDir = reward.id().crateId().value();
        String rewardFileName = reward.id().rewardId().value();

        return this.rewardsDir.resolve(crateDir).resolve(FileConfig.withExtension(rewardFileName));
    }

    public Optional<Path> getOrCreateRewardFile(Reward reward) {
        Path file = this.getRewardFile(reward);
        if (!Files.exists(file)) {
            try {
                Files.createDirectories(file.getParent());
                return Optional.of(Files.createFile(file));
            }
            catch (IOException exception) {
                LOGGER.error("Could not create reward file '{}'", file);
                LOGGER.error("Reason: ", exception);
                return Optional.empty();
            }
        }

        return Optional.of(file);
    }

    public boolean deleteRewardFile(Reward reward) {
        Path file = this.getRewardFile(reward);
        if (!Files.exists(file)) return true;

        try {
            return Files.deleteIfExists(file);
        }
        catch (IOException exception) {
            LOGGER.error("Reward file '{}' can not be deleted.", file);
            LOGGER.error("Reason: ", exception);
            return false;
        }
    }

    public List<Reward> readRewards() {
        List<Reward> rewards = new ArrayList<>();

        FileUtil.findFiles(this.rewardsDir, Files::isDirectory).forEach(crateDir -> {
            String crateName = crateDir.getFileName().toString();
            Identifier crateId = IdentifierParser.parse(crateName).orElse(null);
            if (crateId == null) {
                LOGGER.error("Invalid crate ID '{}'", crateName);
                return;
            }

            FileUtil.findYamlFiles(crateDir).forEach(file -> {
                try {
                    rewards.add(this.readReward(crateId, file));
                }
                catch (ModelLoadException exception) {
                    LOGGER.error("Reward '{}' can not be loaded.", file);
                    LOGGER.error("Reason: ", exception);
                }
            });
        });


        return rewards;
    }

    public Reward readReward(Identifier crateId, Path file) throws ModelLoadException {
        String name = FileUtil.getNameWithoutExtension(file);

        Identifier rewardId = IdentifierParser.parseSanitized(name)
            .orElseThrow(() -> new ModelLoadException("Invalid file name"));

        RewardId id = new RewardId(crateId, rewardId);

        FileConfig config = FileConfig.load(file);

        RewardBase base = config.getOrSet("base",
            StandardRewardBase.class,
            StandardRewardBase.createDefault()
        );

        RewardPreview preview = config.getOrSet("preview",
            StandardRewardPreview.class,
            StandardRewardPreview.createDefault()
        );

        StandardRewardBuilder builder = new StandardRewardBuilder(id);

        builder.base(base);
        builder.preview(preview);

        this.extensions.getEntries().forEach(extension -> {
            extension.onRead(config, builder);
        });

        config.saveChanges();

        return builder.build();
    }

    public void writeReward(Reward reward) {
        Path file = this.getOrCreateRewardFile(reward).orElse(null);
        if (file == null) {
            LOGGER.error("Reward '{}' can not be saved.", file);
            return;
        }

        FileConfig config = FileConfig.load(file);

        config.set("base", reward.getBase());
        config.set("preview", reward.getPreview());

        this.extensions.getEntries().forEach(extension -> {
            extension.onWrite(config, reward);
        });

        config.saveChanges();
    }
}
