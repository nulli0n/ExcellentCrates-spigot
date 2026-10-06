package su.nightexpress.excellentcrates.api.key.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public interface KeyCommand {

    ExecutableNode createCommand();
}
