package su.nightexpress.excellentcrates.reward.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.excellentcrates.api.reward.dispatcher.RewardMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.placeholder.RewardPlaceholders;
import su.nightexpress.excellentcrates.core.dispatcher.ForwardingCrateMessageDispatcher;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public class DefaultRewardMessageDispatcher extends ForwardingCrateMessageDispatcher implements RewardMessageDispatcher {

    private final CrateMessageDispatcher delegate;
    private final RewardPlaceholders     rewardPlaceholders;

    public DefaultRewardMessageDispatcher(CrateMessageDispatcher delegate, RewardPlaceholders rewardPlaceholders) {
        super();
        this.delegate = delegate;
        this.rewardPlaceholders = rewardPlaceholders;
    }

    @Override
    protected CrateMessageDispatcher getDelegate() {
        return this.delegate;
    }

    @Override
    public void sendAll(CommandSender sender, Crate crate, Reward reward, @Nullable Player player,
                        MessageLocale locale) {
        this.delegate.sendAll(sender, crate, player, locale, this.rewardPlaceholders.allPlaceholders(crate, reward));
    }

    @Override
    public void sendAll(CommandSender sender, Crate crate, Reward reward, @Nullable Player player, MessageLocale locale,
                        PlaceholderApplier extra) {
        this.delegate.sendAll(sender, crate, player, locale, ctx -> ctx
            .apply(this.rewardPlaceholders.allPlaceholders(crate, reward))
            .apply(extra)
        );
    }

    @Override
    public void sendBase(CommandSender sender, Reward reward, MessageLocale locale) {
        this.delegate.send(sender, locale, this.rewardPlaceholders.basePlaceholders(reward));
    }

    @Override
    public void sendBase(CommandSender sender, Reward reward, MessageLocale locale, PlaceholderApplier extra) {
        this.delegate.send(sender, locale, ctx -> ctx
            .apply(this.rewardPlaceholders.basePlaceholders(reward))
            .apply(extra)
        );
    }

    @Override
    public void sendBase(CommandSender sender, Crate crate, Reward reward, MessageLocale locale) {
        this.delegate.sendBase(sender, crate, locale, this.rewardPlaceholders.basePlaceholders(reward));
    }

    @Override
    public void sendBase(CommandSender sender, Crate crate, Reward reward, MessageLocale locale,
                         PlaceholderApplier extra) {
        this.delegate.sendBase(sender, crate, locale, ctx -> ctx
            .apply(this.rewardPlaceholders.basePlaceholders(reward))
            .apply(extra)
        );
    }
}
