package su.nightexpress.excellentcrates.core.dispatcher;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public abstract class ForwardingMessageDispatcher implements MessageDispatcher {

    protected abstract MessageDispatcher getDelegate();

    @Override
    public void send(CommandSender sender, MessageLocale locale) {
        this.getDelegate().send(sender, locale);
    }

    @Override
    public void send(CommandSender sender, MessageLocale locale, PlaceholderApplier consumer) {
        this.getDelegate().send(sender, locale, consumer);
    }

    @Override
    public void send(CommandSender sender, MessageLocale locale, PlaceholderContext context) {
        this.getDelegate().send(sender, locale, context);
    }

    @Override
    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale) {
        this.getDelegate().send(receivers, locale);
    }

    @Override
    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderApplier consumer) {
        this.getDelegate().send(receivers, locale, consumer);
    }

    @Override
    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderContext context) {
        this.getDelegate().send(receivers, locale, context);
    }

    @Override
    public void broadcast(MessageLocale locale) {
        this.getDelegate().broadcast(locale);
    }

    @Override
    public void broadcast(MessageLocale locale, PlaceholderApplier consumer) {
        this.getDelegate().broadcast(locale, consumer);
    }

    @Override
    public void broadcast(MessageLocale locale, PlaceholderContext context) {
        this.getDelegate().broadcast(locale, context);
    }
}
