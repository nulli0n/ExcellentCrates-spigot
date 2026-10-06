package su.nightexpress.excellentcrates.api.crate.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.action.ActionResult;
import su.nightexpress.engine.action.FeedbackHandler;
import su.nightexpress.excellentcrates.api.crate.Crate;

@NullMarked
public interface CrateFeedbackHandler extends FeedbackHandler {

    @Override
    CrateMessageDispatcher getDispatcher();

    default boolean handleFeedbackBase(CommandSender sender, Crate crate, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> {
            this.getDispatcher().sendBase(sender, crate, locale, ctx);
        });
    }

    default boolean handleFeedbackAll(CommandSender sender, Crate crate, @Nullable Player player, ActionResult result) {
        return result.handleFeedback((locale, ctx) -> {
            this.getDispatcher().sendAll(sender, crate, player, locale, ctx);
        });
    }
}