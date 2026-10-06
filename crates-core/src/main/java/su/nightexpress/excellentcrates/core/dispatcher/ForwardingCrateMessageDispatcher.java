package su.nightexpress.excellentcrates.core.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public abstract class ForwardingCrateMessageDispatcher extends ForwardingMessageDispatcher implements CrateMessageDispatcher {

    protected abstract CrateMessageDispatcher getDelegate();

    @Override
    public void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale) {
        this.getDelegate().sendAll(sender, crate, player, locale);
    }

    @Override
    public void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale,
                        PlaceholderApplier extra) {
        this.getDelegate().sendAll(sender, crate, player, locale, extra);
    }

    @Override
    public void sendBase(CommandSender player, Crate crate, MessageLocale locale) {
        this.getDelegate().sendBase(player, crate, locale);
    }

    @Override
    public void sendBase(CommandSender player, Crate crate, MessageLocale locale, PlaceholderApplier extra) {
        this.getDelegate().sendBase(player, crate, locale, extra);
    }
}
