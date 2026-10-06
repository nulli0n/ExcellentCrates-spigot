package su.nightexpress.excellentcrates.api.reward.commands;

import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface RewardCommandPool {

    UUID getId();

    List<String> getCommands();

    void setCommands(List<String> commands);

    void addCommand(String command);

    void removeCommand(int index);

    void clearCommands();

    double getWeight();

    void setWeight(double weight);
}
