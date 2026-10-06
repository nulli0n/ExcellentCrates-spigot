package su.nightexpress.excellentcrates.rarity;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.component.BaseModuleBootstrap;
import su.nightexpress.engine.component.ComponentBundle;
import su.nightexpress.engine.component.CoreDependencies;
import su.nightexpress.engine.service.ServiceRegistry;
import su.nightexpress.engine.ui.CoreUIService;
import su.nightexpress.excellentcrates.api.CoreServices;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;
import su.nightexpress.excellentcrates.rarity.api.RarityAPIProvider;
import su.nightexpress.excellentcrates.rarity.data.RarityDataConfiguration;
import su.nightexpress.excellentcrates.rarity.data.RarityDataService;
import su.nightexpress.excellentcrates.rarity.data.component.RarityDataLoader;
import su.nightexpress.excellentcrates.rarity.io.RarityIOConfiguration;
import su.nightexpress.excellentcrates.rarity.io.RarityIOService;
import su.nightexpress.excellentcrates.rarity.reward.component.DefaultRarityComponent;
import su.nightexpress.excellentcrates.rarity.reward.component.codec.RarityComponentCodec;
import su.nightexpress.excellentcrates.rarity.reward.component.editor.RarityComponentEditorBootstrapContext;
import su.nightexpress.excellentcrates.rarity.reward.component.extension.RarityComponentDataExtension;
import su.nightexpress.excellentcrates.rarity.reward.placeholder.RarityRewardPlaceholder;
import su.nightexpress.nightcore.configuration.codec.ConfigCodecs;

@NullMarked
public class RarityModuleBootstrap extends BaseModuleBootstrap {

    @Override
    public void onRegister(ServiceRegistry services, CoreDependencies dependencies) {
        CratesPlugin plugin = dependencies.plugin();
        CoreUIService coreUI = dependencies.uiService();

        ConfigCodecs.register(DefaultRarityComponent.class, RarityComponentCodec.INSTANCE);

        RarityRepository rarities = new RarityRepository();

        RarityIOService ioService = RarityIOConfiguration.configure(dependencies);
        RarityDataService dataService = RarityDataConfiguration.configure(rarities, ioService);

        this.registerComponent(new RarityDataLoader(dataService, rarities));

        this.bridge.onAvailable(CoreServices.REWARDS, rewardsApi -> {
            RewardRegistry rewards = rewardsApi.getRegistry();
            RewardMessageDispatcher rewardDispatcher = rewardsApi.getMessageDispatcher();

            RarityComponentEditorBootstrapContext editorContext = new RarityComponentEditorBootstrapContext(
                plugin, coreUI, rewardDispatcher, rewards, rarities
            );
            this.registerComponent(editorContext);

            rewardsApi.getData().registerExtension(new RarityComponentDataExtension(rarities));
            rewardsApi.getEditor().registerExtension(editorContext.getEditorExtension());
            rewardsApi.getPlaceholders().registerPlaceholder(new RarityRewardPlaceholder(rarities));
            rewardsApi.getView().setColorProvider((reward, defaultColor) -> {
                RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
                if (component == null || !component.isEnabled()) return defaultColor;

                Rarity rarity = rarities.get(component.getRarityId());
                return rarity == null ? defaultColor : rarity.getColor();
            });
        });

        services.register(CoreServices.RARITY, new RarityAPIProvider(rarities, dataService));
    }

    @Override
    protected ComponentBundle buildComponent(ServiceRegistry services, CoreDependencies dependencies) {
        return new RarityModule();
    }
}
