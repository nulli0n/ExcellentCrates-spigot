package su.nightexpress.excellentcrates.keys.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.key.CrateKey;
import su.nightexpress.excellentcrates.api.key.command.KeyCommand;
import su.nightexpress.excellentcrates.api.key.registry.KeyResolver;
import su.nightexpress.excellentcrates.keys.command.argument.KeyArgumentType;
import su.nightexpress.excellentcrates.keys.command.controller.KeyCommandRegistrar;
import su.nightexpress.excellentcrates.keys.config.settings.KeyCoreSettings;
import su.nightexpress.nightcore.commands.Arguments;

@NullMarked
public final class KeyCommandBootstrapContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("keys.commands");
    private static final String     NAME = "Commands";

    public final TinyRegistry<KeyCommand> commands;

    public KeyCommandBootstrapContext(CratesPlugin plugin,
                                      KeyResolver resolver,
                                      ReadOnlySettings<KeyCoreSettings> settings) {
        super(ID, NAME);
        this.commands = new SimpleRegistry<>();

        Arguments.register(CrateKey.class, new KeyArgumentType(resolver));

        this.addComponent(new KeyCommandRegistrar(plugin, this.commands, settings));
    }
}
