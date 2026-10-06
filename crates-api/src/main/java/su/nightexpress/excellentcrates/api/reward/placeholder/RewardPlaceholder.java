package su.nightexpress.excellentcrates.api.reward.placeholder;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public interface RewardPlaceholder {

    Consumer<PlaceholderContext.Builder> applyInCrate(CrateRewardEntry entry, Crate crate, Reward reward,
                                                      @Nullable Player player);

    Consumer<PlaceholderContext.Builder> applyBase(Reward reward, @Nullable Player player);
}
