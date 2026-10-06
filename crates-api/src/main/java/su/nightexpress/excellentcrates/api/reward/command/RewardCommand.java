package su.nightexpress.excellentcrates.api.reward.command;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.commands.tree.ExecutableNode;

@NullMarked
public interface RewardCommand {

    ExecutableNode createCommand();
}
