package su.nightexpress.excellentcrates.keys.dispatcher;

import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.dispatcher.KeyMessageDispatcher;
import su.nightexpress.excellentcrates.api.key.placeholder.KeyPlaceholders;
import su.nightexpress.excellentcrates.core.dispatcher.ForwardingCrateMessageDispatcher;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public class DefaultKeyMessageDispatcher extends ForwardingCrateMessageDispatcher implements KeyMessageDispatcher {

    private final CrateMessageDispatcher delegate;
    private final KeyPlaceholders        placeholders;

    public DefaultKeyMessageDispatcher(CrateMessageDispatcher delegate, KeyPlaceholders placeholders) {
        super();
        this.delegate = delegate;
        this.placeholders = placeholders;
    }

    @Override
    protected CrateMessageDispatcher getDelegate() {
        return this.delegate;
    }

    @Override
    public void sendBase(CommandSender sender, CrateKey key, MessageLocale locale) {
        this.send(sender, locale, this.placeholders.basePlaceholders(key));
    }

    @Override
    public void sendBase(CommandSender sender, CrateKey key, MessageLocale locale, PlaceholderApplier extra) {
        this.send(sender, locale, builder -> builder
            .apply(this.placeholders.basePlaceholders(key))
            .apply(extra)
        );
    }

    @Override
    public void sendBase(CommandSender sender, Crate crate, CrateKey key, MessageLocale locale) {
        this.sendBase(sender, crate, locale, this.placeholders.basePlaceholders(key));
    }

    @Override
    public void sendBase(CommandSender sender, Crate crate, CrateKey key, MessageLocale locale,
                         PlaceholderApplier extra) {
        this.sendBase(sender, crate, locale, builder -> builder
            .apply(this.placeholders.basePlaceholders(key))
            .apply(extra)
        );
    }
}
