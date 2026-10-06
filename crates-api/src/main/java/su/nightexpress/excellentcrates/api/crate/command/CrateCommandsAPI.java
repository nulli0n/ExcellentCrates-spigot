package su.nightexpress.excellentcrates.api.crate.command;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface CrateCommandsAPI {

    void registerCommand(CrateCommand command);
}
