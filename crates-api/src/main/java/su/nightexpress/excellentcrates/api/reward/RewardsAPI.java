package su.nightexpress.excellentcrates.api.reward;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.service.PluginAPI;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommandsAPI;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.data.RewardDataAPI;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.editor.RewardEditorAPI;
import su.nightexpress.excellentcrates.api.reward.evaluation.RewardEvaluator;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantAPI;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.api.reward.preview.RewardPreviewAPI;
import su.nightexpress.excellentcrates.api.reward.quota.RewardQuotaAPI;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;

@NullMarked
public interface RewardsAPI extends PluginAPI {

    RewardMessageDispatcher getMessageDispatcher();

    RewardRegistry getRegistry();

    RewardPlaceholders getPlaceholders();

    RewardCommandsAPI getCommands();

    RewardDataAPI getData();

    RewardGrantAPI getGrant();

    RewardEvaluator getEvaluator();

    RewardPreviewAPI getView();

    RewardEditorAPI getEditor();

    RewardQuotaAPI getQuota();

    @Nullable
    Reward getReward(Identifier id);

    Set<Reward> getRewards();

    @Nullable
    CrateRewardsComponent getRewardsComponent(Crate crate);

    List<Reward> getAvailableRewards(Crate crate, Player player);

    Optional<Reward> rollReward(Crate crate, Player player);

    Optional<Reward> rollReward(Crate crate, Rarity rarity, Player player);
}
