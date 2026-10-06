package su.nightexpress.excellentcrates.core.dispatcher;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.dispatcher.Prefixed;
import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.locale.message.LangMessage;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public class StandardMessageDispatcher implements MessageDispatcher {

    private final Prefixed prefixed;

    public StandardMessageDispatcher(Prefixed prefix) {
        this.prefixed = prefix;
    }

    public LangMessage getPrefixed(MessageLocale locale) {
        return prefixed == null ? locale.value() : locale.withPrefix(prefixed.getPrefix());
    }

    public void send(CommandSender sender, MessageLocale locale) {
        this.getPrefixed(locale).send(sender);
    }

    public void send(CommandSender sender, MessageLocale locale, PlaceholderApplier consumer) {
        this.getPrefixed(locale).sendWith(sender, consumer);
    }

    public void send(CommandSender sender, MessageLocale locale, PlaceholderContext context) {
        this.getPrefixed(locale).sendWith(sender, context);
    }

    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale) {
        this.getPrefixed(locale).send(receivers);
    }

    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderApplier consumer) {
        this.getPrefixed(locale).sendWith(receivers, consumer);
    }

    public void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderContext context) {
        this.getPrefixed(locale).sendWith(receivers, context);
    }

    public void broadcast(MessageLocale locale) {
        this.getPrefixed(locale).broadcast();
    }

    public void broadcast(MessageLocale locale, PlaceholderApplier consumer) {
        this.getPrefixed(locale).broadcastWith(consumer);
    }

    public void broadcast(MessageLocale locale, PlaceholderContext context) {
        this.getPrefixed(locale).broadcastWith(context);
    }
}
