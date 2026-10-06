package su.nightexpress.excellentcrates.engine.dispatcher;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.dispatcher.MessageDispatcher;
import su.nightexpress.engine.placeholder.PlaceholderApplier;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.dispatcher.CrateMessageDispatcher;
import su.nightexpress.excellentcrates.api.crate.placeholder.CratePlaceholders;
import su.nightexpress.excellentcrates.core.dispatcher.ForwardingMessageDispatcher;
import su.nightexpress.nightcore.locale.entry.MessageLocale;

@NullMarked
public class DefaultCrateMessageDispatcher extends ForwardingMessageDispatcher implements CrateMessageDispatcher {

    private final MessageDispatcher delegate;
    private final CratePlaceholders placeholders;

    public DefaultCrateMessageDispatcher(MessageDispatcher dispatcher, CratePlaceholders placeholders) {
        super();
        this.delegate = dispatcher;
        this.placeholders = placeholders;
    }

    @Override
    protected MessageDispatcher getDelegate() {
        return this.delegate;
    }

    @Override
    public void sendBase(CommandSender player, Crate crate, MessageLocale locale) {
        this.delegate.send(player, locale, this.placeholders.basePlaceholders(crate));
    }

    @Override
    public void sendBase(CommandSender player, Crate crate, MessageLocale locale, PlaceholderApplier extra) {
        this.delegate.send(player, locale, builder -> builder
            .apply(this.placeholders.basePlaceholders(crate))
            .apply(extra)
        );
    }

    @Override
    public void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale) {
        this.delegate.send(sender, locale, this.placeholders.allPlaceholders(crate, player));
    }

    @Override
    public void sendAll(CommandSender sender, Crate crate, @Nullable Player player, MessageLocale locale,
                        PlaceholderApplier extra) {
        this.delegate.send(sender, locale, builder -> builder
            .apply(this.placeholders.allPlaceholders(crate, player))
            .apply(extra)
        );
    }
}
