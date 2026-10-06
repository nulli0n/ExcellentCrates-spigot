package su.nightexpress.excellentcrates.api.crate.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public interface CrateMessageDispatcher extends MessageDispatcher {

    default boolean handleFeedback(Player player, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> this.send(player, locale, ctx));
    }

    default boolean handleFeedbackBase(Player player, Crate crate, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> this.sendBase(player, crate, locale, ctx));
    }

    void sendBase(CommandSender sender, Crate crate, MessageLocale locale);

    void sendBase(CommandSender sender, Crate crate, MessageLocale locale, PlaceholderApplier extra);

    void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale);

    void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale,
                 PlaceholderApplier extra);

    default void sendAll(Player player, Crate crate, MessageLocale locale) {
        this.sendAll(player, crate, player, locale);
    }

    default void sendAll(Player player, Crate crate, MessageLocale locale, PlaceholderApplier extra) {
        this.sendAll(player, crate, player, locale, extra);
    }
}
