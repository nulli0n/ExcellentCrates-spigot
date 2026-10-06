package su.nightexpress.excellentcrates.api.reward.grant;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public interface RewardGrantProcessor {

    void execute(Player player, Crate crate, Reward reward, PlaceholderContext placeholders);
}
