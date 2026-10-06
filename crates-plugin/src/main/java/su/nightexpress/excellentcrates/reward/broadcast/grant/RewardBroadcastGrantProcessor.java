package su.nightexpress.excellentcrates.reward.broadcast.grant;

import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.broadcast.RewardBroadcastComponent;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponentKeys;
import su.nightexpress.excellentcrates.api.reward.grant.RewardGrantProcessor;
import su.nightexpress.excellentcrates.reward.broadcast.message.BroadcastMessageService;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class RewardBroadcastGrantProcessor implements RewardGrantProcessor {

    private final BroadcastMessageService messageService;

    public RewardBroadcastGrantProcessor(BroadcastMessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Player player, Crate crate, Reward reward, PlaceholderContext placeholders) {
        RewardBroadcastComponent component = reward.getComponentOrNull(RewardComponentKeys.BROADCAST);
        if (component == null || !component.isEnabled()) return;

        this.messageService.broadcast(player, crate, reward, placeholders);
    }
}
