package su.nightexpress.excellentcrates.rarity.reward.component.extension;

import org.jspecify.annotations.NullMarked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.data.RewardBuilder;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.rarity.reward.component.DefaultRarityComponent;
import su.nightexpress.excellentcrates.rarity.reward.component.codec.RarityComponentCodec;
import su.nightexpress.nightcore.config.FileConfig;

@NullMarked
public class RarityComponentDataExtension implements RewardDataExtension {

    private static final Logger LOGGER = LoggerFactory.getLogger(RarityComponentDataExtension.class);

    private final RarityRegistry registry;

    public RarityComponentDataExtension(RarityRegistry registry) {
        this.registry = registry;
    }

    private DefaultRarityComponent createDefaultRarityComponent() {
        Rarity defaultRarity = this.registry.findLessWeighted().orElse(null);
        if (defaultRarity == null) {
            LOGGER.error("Can not find default rarity. The Rarity component will be disabled.");
            return new DefaultRarityComponent(false, new Identifier("null"));
        }

        return new DefaultRarityComponent(true, defaultRarity.id());
    }

    @Override
    public void onBuild(RewardBuilder builder) {
        builder.component(RewardComponentKeys.RARITY, this.createDefaultRarityComponent());
    }

    @Override
    public void onRead(FileConfig config, RewardBuilder builder) {
        RarityComponent component = config.getOrSet("Rarity",
            RarityComponentCodec.INSTANCE,
            this.createDefaultRarityComponent()
        );

        builder.component(RewardComponentKeys.RARITY, component);
    }

    @Override
    public void onWrite(FileConfig config, Reward reward) {
        RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
        config.set("Rarity", component);
    }

    @Override
    public void onCreate(Reward reward) {

    }

    @Override
    public void onDelete(Reward reward) {

    }

    @Override
    public void onLoad(Reward reward) {

    }

    @Override
    public void onUnload(Reward reward) {

    }
}
