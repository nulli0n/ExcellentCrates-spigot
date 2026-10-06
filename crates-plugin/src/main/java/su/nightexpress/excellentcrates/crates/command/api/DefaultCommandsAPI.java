package su.nightexpress.excellentcrates.crates.command.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommand;
import su.nightexpress.excellentcrates.api.crate.command.CrateCommandsAPI;

@NullMarked
public class DefaultCommandsAPI implements CrateCommandsAPI {

    private final TinyRegistry<CrateCommand> commands;

    public DefaultCommandsAPI(TinyRegistry<CrateCommand> commands) {
        this.commands = commands;
    }

    @Override
    public void registerCommand(CrateCommand command) {
        this.commands.register(command);
    }
}
