package su.nightexpress.excellentcrates.api.reward.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.reward.Reward;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public interface RewardMessageDispatcher extends CrateMessageDispatcher {

    default boolean handleFeedback(Player player, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> this.send(player, locale, ctx));
    }

    default boolean handleFeedbackBase(Player player, Reward reward, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> this.sendBase(player, reward, locale, ctx));
    }

    void sendBase(CommandSender sender, Reward reward, MessageLocale locale);

    void sendBase(CommandSender sender, Reward reward, MessageLocale locale, PlaceholderApplier extra);

    void sendBase(CommandSender sender, Crate crate, Reward reward, MessageLocale locale);

    void sendBase(CommandSender sender, Crate crate, Reward reward, MessageLocale locale,
                  PlaceholderApplier extra);

    void sendAll(CommandSender sender, Crate crate, Reward reward, @Nullable Player player, MessageLocale locale);

    void sendAll(CommandSender sender, Crate crate, Reward reward, @Nullable Player player, MessageLocale locale,
                 PlaceholderApplier extra);

    default void sendAll(Player player, Crate crate, Reward reward, MessageLocale locale) {
        this.sendAll(player, crate, reward, player, locale);
    }

    default void sendAll(Player player, Crate crate, Reward reward, MessageLocale locale, PlaceholderApplier extra) {
        this.sendAll(player, crate, reward, player, locale, extra);
    }
}
