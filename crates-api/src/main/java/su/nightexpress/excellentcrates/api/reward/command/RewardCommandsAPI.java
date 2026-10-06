package su.nightexpress.excellentcrates.api.reward.command;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RewardCommandsAPI {

    void registerCommand(RewardCommand command);
}
