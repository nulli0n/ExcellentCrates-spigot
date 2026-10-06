package su.nightexpress.excellentcrates.reward.command.api;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.registry.TinyRegistry;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommand;
import su.nightexpress.excellentcrates.api.reward.command.RewardCommandsAPI;

@NullMarked
public class DefaultRewardCommandsAPI implements RewardCommandsAPI {

    private final TinyRegistry<RewardCommand> commands;

    public DefaultRewardCommandsAPI(TinyRegistry<RewardCommand> registry) {
        this.commands = registry;
    }

    @Override
    public void registerCommand(RewardCommand command) {
        this.commands.register(command);
    }
}
