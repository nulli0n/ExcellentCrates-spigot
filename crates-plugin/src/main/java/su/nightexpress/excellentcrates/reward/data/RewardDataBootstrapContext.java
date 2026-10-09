package su.nightexpress.excellentcrates.reward.data;

import java.nio.file.Path;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.reward.data.RewardDataAPI;
import su.nightexpress.excellentcrates.api.reward.data.extension.RewardDataExtension;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.reward.config.settings.RewardCoreSettings;
import su.nightexpress.excellentcrates.reward.data.codec.RewardBaseCodec;
import su.nightexpress.excellentcrates.reward.data.codec.RewardIdCodec;
import su.nightexpress.excellentcrates.reward.data.codec.RewardPreviewCodec;
import su.nightexpress.excellentcrates.reward.data.controller.RewardDataLoader;
import su.nightexpress.excellentcrates.reward.data.controller.RewardDataSaveController;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardBase;
import su.nightexpress.excellentcrates.reward.data.reward.StandardRewardPreview;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardDataBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("rewards.data");
    private static final String     NAME = "IO/Data";

    private static final String REWARDS_DIR = "reward";

    public final TinyRegistry<RewardDataExtension> extensions;

    public final RewardIOService   ioService;
    public final RewardDataService dataService;

    public final RewardDataAPI api;

    public RewardDataBootstrapContext(CratesPlugin plugin,
                                      RewardRegistry registry,
                                      ReadOnlySettings<RewardCoreSettings> settings) {
        super(ID, NAME);
        this.extensions = new SimpleRegistry<>();

        ConfigCodecs.register(RewardId.class, RewardIdCodec.INSTANCE);
        ConfigCodecs.register(StandardRewardBase.class, RewardBaseCodec.INSTANCE);
        ConfigCodecs.register(StandardRewardPreview.class, RewardPreviewCodec.INSTANCE);

        Path rewardsDir = plugin.objectsPath().resolve(REWARDS_DIR);

        this.ioService = new RewardIOService(rewardsDir, this.extensions);
        this.dataService = new RewardDataService(this.ioService, registry, this.extensions);

        this.api = new DefaultRewardDataAPI(this.extensions, this.dataService);

        this.addComponent(new RewardDataLoader(this.dataService, registry));
        this.addComponent(new RewardDataSaveController(plugin, this.dataService, settings));
    }
}
