package su.nightexpress.excellentcrates.rarity.reward.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.rarity.Rarity;
import su.nightexpress.excellentcrates.api.rarity.registry.RarityRegistry;
import su.nightexpress.excellentcrates.api.rarity.reward.RarityComponent;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholder;
import su.nightexpress.excellentcrates.core.SharedPlaceholders;
import su.nightexpress.nightcore.core.config.CoreLang;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext.Builder;

@NullMarked
public class RarityRewardPlaceholder implements RewardPlaceholder {

    private final RarityRegistry registry;

    public RarityRewardPlaceholder(RarityRegistry registry) {
        this.registry = registry;
    }

    @Override
    public Consumer<Builder> applyInCrate(CrateRewardEntry crateReward, Crate crate, Reward reward,
                                          @Nullable Player player) {
        return ctx -> {

        };
    }

    @Override
    public Consumer<Builder> applyBase(Reward reward, @Nullable Player player) {
        return ctx -> {
            RarityComponent component = reward.getComponentOrNull(RewardComponentKeys.RARITY);
            ctx.with(SharedPlaceholders.REWARD_RARITY, () -> {
                if (component == null) return CoreLang.OTHER_NONE.text();

                Identifier rarityId = component.getRarityId();
                Rarity rarity = this.registry.get(rarityId);
                return rarity == null ? rarityId.value() : rarity.getName();
            });

            if (component != null) {
                ctx.with(SharedPlaceholders.REWARD_HAS_RARITY_MARKER, () -> String.valueOf(true));
            }
        };
    }
}
