package su.nightexpress.engine.dispatcher;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.nightcore.locale.entry.MessageLocale;
import su.nightexpress.nightcore.util.placeholder.PlaceholderContext;

@NullMarked
public interface MessageDispatcher {

    void send(CommandSender sender, MessageLocale locale);

    void send(CommandSender sender, MessageLocale locale, PlaceholderApplier consumer);

    void send(CommandSender sender, MessageLocale locale, PlaceholderContext context);

    void send(Collection<? extends CommandSender> receivers, MessageLocale locale);

    void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderApplier consumer);

    void send(Collection<? extends CommandSender> receivers, MessageLocale locale, PlaceholderContext context);

    void broadcast(MessageLocale locale);

    void broadcast(MessageLocale locale, PlaceholderApplier consumer);

    void broadcast(MessageLocale locale, PlaceholderContext context);
}
