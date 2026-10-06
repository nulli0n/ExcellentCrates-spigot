package su.nightexpress.excellentcrates.api.crate.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public interface CrateCommand {

    ExecutableNode createCommand();
}
