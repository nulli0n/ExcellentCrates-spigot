package su.nightexpress.excellentcrates.reward.feature.commands.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.commands.RewardCommandPool;

@NullMarked
public class StandrdRewardCommandPool implements RewardCommandPool {

    private final UUID id;

    private List<String> commands;
    private double       weight;

    public StandrdRewardCommandPool(UUID id, List<String> commands, double weight) {
        this.id = id;
        this.commands = new ArrayList<>(commands);
        this.weight = weight;
    }

    public static StandrdRewardCommandPool createDefault() {
        return new StandrdRewardCommandPool(UUID.randomUUID(), List.of(), 0.0);
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public List<String> getCommands() {
        return Collections.unmodifiableList(this.commands);
    }

    @Override
    public void setCommands(List<String> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void addCommand(String command) {
        this.commands.add(command);
    }

    @Override
    public void removeCommand(int index) {
        this.commands.remove(index);
    }

    @Override
    public void clearCommands() {
        this.commands.clear();
    }

    @Override
    public double getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(double weight) {
        this.weight = weight;
    }
}
