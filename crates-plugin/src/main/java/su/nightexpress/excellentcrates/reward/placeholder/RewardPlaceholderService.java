package su.nightexpress.excellentcrates.reward.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponentKeys;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.util.NumberUtil;

@NullMarked
public class RewardPlaceholderService implements RewardPlaceholders {

    private final TinyRegistry<RewardPlaceholder> placeholders;

    public RewardPlaceholderService() {
        this.placeholders = new SimpleRegistry<>();
    }

    @Override
    public void registerPlaceholder(RewardPlaceholder placeholder) {
        this.placeholders.register(placeholder);
    }

    @Override
    public TinyRegistry<RewardPlaceholder> getPlaceholders() {
        return this.placeholders;
    }

    public PlaceholderApplier allPlaceholders(Crate crate, Reward reward) {
        return this.allPlaceholders(crate, reward, null);
    }

    public PlaceholderApplier allPlaceholders(Crate crate, Reward reward, @Nullable Player player) {
        return ctx -> ctx
            .apply(this.inCratePlaceholders(crate, reward, player))
            .apply(this.basePlaceholders(reward, player));
    }

    public PlaceholderApplier inCratePlaceholders(Crate crate, Reward reward) {
        return this.inCratePlaceholders(crate, reward, null);
    }

    public PlaceholderApplier inCratePlaceholders(Crate crate, Reward reward,
                                                  @Nullable Player player) {
        return ctx -> {
            CrateRewardsComponent component = crate.getComponentOrNull(CrateComponentKeys.REWARDS);
            if (component == null) return;

            CrateRewardEntry rewardEntry = component.getReward(reward.id());
            if (rewardEntry == null) return; // Reward is not in the crate, nothing to replace.

            ctx.with(SharedPlaceholders.REWARD_WEIGHT, () -> NumberUtil.format(rewardEntry.getWeight()));

            this.placeholders.forEach(placeholder -> {
                ctx.apply(placeholder.applyInCrate(rewardEntry, crate, reward, player));
            });
        };
    }

    public PlaceholderApplier basePlaceholders(Reward reward) {
        return this.basePlaceholders(reward, null);
    }

    public PlaceholderApplier basePlaceholders(Reward reward, @Nullable Player player) {
        return ctx -> {
            ctx.with(SharedPlaceholders.REWARD_ID, () -> reward.idString());

            this.placeholders.forEach(placeholder -> ctx.apply(placeholder.applyBase(reward, player)));
        };
    }
}
