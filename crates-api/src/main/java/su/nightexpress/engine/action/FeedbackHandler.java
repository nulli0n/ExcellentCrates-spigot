package su.nightexpress.engine.action;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;

@NullMarked
public interface FeedbackHandler {

    MessageDispatcher getDispatcher();

    default boolean handleFeedback(CommandSender sender, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> {
            this.getDispatcher().send(sender, locale, ctx);
        });
    }
}
