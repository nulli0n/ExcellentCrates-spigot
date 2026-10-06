package su.nightexpress.excellentcrates.reward.crate.component;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardEntryCodec;
import su.nightexpress.excellentcrates.reward.crate.component.codec.RewardsComponentCodec;
import su.nightexpress.excellentcrates.reward.crate.component.extension.RewardsComponentDataExtension;
import su.nightexpress.excellentcrates.reward.crate.component.extension.RewardsComponentExtension;
import su.nightexpress.excellentcrates.reward.crate.component.lang.RewardComponentLang;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardEntry;
import su.nightexpress.excellentcrates.reward.crate.component.model.DefaultRewardsComponent;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RewardComponentBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("reward.component");
    private static final String     NAME = "Crate Component";

    private final RewardsComponentExtension     crateDataExtension;
    private final RewardsComponentDataExtension dataExtension;

    public RewardComponentBootstrapContext(CratesPlugin plugin, CrateRegistry crateRegistry) {
        super(ID, NAME);

        plugin.injectLang(RewardComponentLang.class);

        ConfigCodecs.register(DefaultRewardEntry.class, RewardEntryCodec.INSTANCE);
        ConfigCodecs.register(DefaultRewardsComponent.class, RewardsComponentCodec.INSTANCE);

        this.crateDataExtension = new RewardsComponentExtension();
        this.dataExtension = new RewardsComponentDataExtension(crateRegistry);
    }

    public RewardsComponentExtension getCrateDataExtension() {
        return this.crateDataExtension;
    }

    public RewardsComponentDataExtension getDataExtension() {
        return this.dataExtension;
    }
}
