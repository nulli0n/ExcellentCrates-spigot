package su.nightexpress.excellentcrates.api.key.dispatcher;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public interface KeyMessageDispatcher extends CrateMessageDispatcher {

    void sendBase(CommandSender sender, CrateKey key, MessageLocale locale);

    void sendBase(CommandSender sender, CrateKey key, MessageLocale locale, PlaceholderApplier extra);

    void sendBase(CommandSender sender, Crate crate, CrateKey key, MessageLocale locale);

    void sendBase(CommandSender sender, Crate crate, CrateKey key, MessageLocale locale,
                  PlaceholderApplier extra);
}
