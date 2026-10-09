package su.nightexpress.excellentcrates.api.reward.placeholder;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;

@NullMarked
public interface RewardPlaceholders {

    void registerPlaceholder(RewardPlaceholder placeholder);

    TinyRegistry<RewardPlaceholder> getPlaceholders();

    PlaceholderApplier allPlaceholders(Crate crate, Reward reward);

    PlaceholderApplier allPlaceholders(Crate crate, Reward reward, @Nullable Player player);

    PlaceholderApplier inCratePlaceholders(Crate crate, Reward reward);

    PlaceholderApplier inCratePlaceholders(Crate crate, Reward reward, @Nullable Player player);

    PlaceholderApplier basePlaceholders(Reward reward);

    PlaceholderApplier basePlaceholders(Reward reward, @Nullable Player player);
}
