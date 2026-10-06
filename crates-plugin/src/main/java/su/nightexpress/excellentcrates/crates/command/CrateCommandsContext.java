package su.nightexpress.excellentcrates.crates.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.bootstrap.context.NamedBootstrapContext;
import su.nightexpress.engine.id.Identifier;
import su.nightexpress.engine.registry.SimpleRegistry;
import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.engine.settings.ReadOnlySettings;
import su.nightexpress.excellentcrates.api.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Crate;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommandsAPI;
import su.nightexpress.excellentcrates.api.crate.registry.CrateRegistry;
import su.nightexpress.excellentcrates.crates.command.api.DefaultCommandsAPI;
import su.nightexpress.excellentcrates.crates.command.argument.CrateArgumentType;
import su.nightexpress.excellentcrates.crates.command.controller.CrateCommandRegistrarController;
import su.nightexpress.excellentcrates.crates.config.settings.CrateCoreSettings;
import su.nightexpress.nightcore.commands.Arguments;

@NullMarked
public final class CrateCommandsContext extends NamedBootstrapContext {

    private static final Identifier ID   = new Identifier("crates.commands");
    private static final String     NAME = "Commands";

    public final TinyRegistry<CrateCommand> commands;

    public final CrateCommandsAPI api;

    public CrateCommandsContext(CratesPlugin plugin,
                                CrateRegistry registry,
                                ReadOnlySettings<CrateCoreSettings> settings) {
        super(ID, NAME);
        this.commands = new SimpleRegistry<>();

        Arguments.register(Crate.class, new CrateArgumentType(registry));

        this.addComponent(new CrateCommandRegistrarController(plugin, this.commands, settings));

        this.api = new DefaultCommandsAPI(this.commands);
    }
}
