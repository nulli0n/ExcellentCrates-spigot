package su.nightexpress.excellentcrates.reward;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.RewardsAPI;
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
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;
import su.nightexpress.excellentcrates.api.reward.registry.RewardRegistry;

@NullMarked
public class DefaultRewardsAPI implements RewardsAPI {

    private final RewardMessageDispatcher dispatcher;
    private final RewardRegistry          registry;
    private final RewardPlaceholders      placeholders;
    private final RewardCommandsAPI       commands;
    private final RewardDataAPI           data;
    private final RewardEvaluator         evaluator;
    private final RewardGrantAPI          grant;
    private final RewardPreviewAPI        view;
    private final RewardEditorAPI         editor;
    private final RewardQuotaAPI          quota;

    public DefaultRewardsAPI(Builder builder) {
        this.dispatcher = Objects.requireNonNull(builder.dispatcher, "RewardMessageDispatcher must not be null");
        this.registry = Objects.requireNonNull(builder.registry, "RewardRegistry must not be null");
        this.placeholders = Objects.requireNonNull(builder.placeholders, "RewardPlaceholders must not be null");
        this.commands = Objects.requireNonNull(builder.commands, "RewardCommandsAPI must not be null");
        this.data = Objects.requireNonNull(builder.data, "RewardDataAPI must not be null");
        this.evaluator = Objects.requireNonNull(builder.evaluator, "RewardEvaluator must not be null");
        this.grant = Objects.requireNonNull(builder.grant, "RewardGrantAPI must not be null");
        this.view = Objects.requireNonNull(builder.view, "RewardViewAPI must not be null");
        this.editor = Objects.requireNonNull(builder.editor, "RewardEditorAPI must not be null");
        this.quota = Objects.requireNonNull(builder.quota, "RewardQuotaAPI must not be null");
    }

    @Override
    public RewardMessageDispatcher getMessageDispatcher() {
        return this.dispatcher;
    }

    @Override
    public RewardRegistry getRegistry() {
        return registry;
    }

    @Override
    public RewardPlaceholders getPlaceholders() {
        return this.placeholders;
    }

    @Override
    public RewardCommandsAPI getCommands() {
        return commands;
    }

    @Override
    public RewardDataAPI getData() {
        return data;
    }

    @Override
    public RewardGrantAPI getGrant() {
        return this.grant;
    }

    @Override
    public RewardEvaluator getEvaluator() {
        return this.evaluator;
    }

    @Override
    public RewardPreviewAPI getView() {
        return this.view;
    }

    @Override
    public RewardEditorAPI getEditor() {
        return this.editor;
    }

    @Override
    public RewardQuotaAPI getQuota() {
        return this.quota;
    }

    @Override
    public @Nullable Reward getReward(RewardId key) {
        return registry.get(key);
    }

    @Override
    public @Nullable Reward getReward(Identifier crateId, Identifier rewardId) {
        return this.registry.resolveReward(crateId, rewardId);
    }

    @Override
    public Set<Reward> getRewards() {
        return registry.values();
    }

    @Override
    public @Nullable CrateRewardsComponent getRewardsComponent(Crate crate) {
        return evaluator.getRewardsComponent(crate);
    }

    @Override
    public List<Reward> getAvailableRewards(Crate crate, Player player) {
        return evaluator.getAvailableRewards(crate, player);
    }

    @Override
    public Optional<Reward> rollReward(Crate crate, Player player) {
        return evaluator.rollReward(crate, player);
    }

    @Override
    public Optional<Reward> rollReward(Crate crate, Rarity rarity, Player player) {
        return evaluator.rollReward(crate, rarity, player);
    }

    public static class Builder {

        private @Nullable RewardMessageDispatcher dispatcher;
        private @Nullable RewardRegistry          registry;
        private @Nullable RewardPlaceholders      placeholders;
        private @Nullable RewardCommandsAPI       commands;
        private @Nullable RewardDataAPI           data;
        private @Nullable RewardEvaluator         evaluator;
        private @Nullable RewardGrantAPI          grant;
        private @Nullable RewardPreviewAPI        view;
        private @Nullable RewardEditorAPI         editor;
        private @Nullable RewardQuotaAPI          quota;

        public Builder setDispatcher(RewardMessageDispatcher dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder setRegistry(RewardRegistry registry) {
            this.registry = registry;
            return this;
        }

        public Builder setPlaceholders(RewardPlaceholders placeholders) {
            this.placeholders = placeholders;
            return this;
        }

        public Builder setData(RewardDataAPI dataService) {
            this.data = dataService;
            return this;
        }

        public Builder setCommands(RewardCommandsAPI commandsAPI) {
            this.commands = commandsAPI;
            return this;
        }

        public Builder setEvaluator(RewardEvaluator evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public Builder setGrant(RewardGrantAPI grant) {
            this.grant = grant;
            return this;
        }

        public Builder setQuota(RewardQuotaAPI quota) {
            this.quota = quota;
            return this;
        }

        public Builder setView(RewardPreviewAPI renderer) {
            this.view = renderer;
            return this;
        }

        public Builder setEditor(RewardEditorAPI editor) {
            this.editor = editor;
            return this;
        }

        public DefaultRewardsAPI build() {
            return new DefaultRewardsAPI(this);
        }
    }
}
