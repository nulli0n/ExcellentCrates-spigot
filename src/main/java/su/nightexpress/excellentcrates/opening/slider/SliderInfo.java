package su.nightexpress.excellentcrates.opening.slider;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.opening.task.TaskStartAction;

public class SliderInfo {

    public enum Mode {
        INHERITANCE, INDEPENDENT
    }

    private final String          id;
    private final TaskStartAction startAction;
    private final double          startChance;
    private final long          startDelay;
    private final int    rollTimes;
    private final long rollTickInterval;
    private final long  rollSlowdownEvery;
    private final long  rollSlowdownTicks;
    private final int[] slots;
    private final Mode  mode;
    private final int[] winSlots;

    public SliderInfo(
        @NotNull String id,
        @NotNull TaskStartAction startAction,
        double startChance,
        long startDelay,
        int rollTimes,
        long rollTickInterval,
        long rollSlowdownEvery,
        long rollSlowdownTicks,
        int[] slot,
        @NotNull SliderInfo.Mode mode,
        int[] winSlots
    ) {
        this.id = id.toLowerCase();
        this.startAction = startAction;
        this.startChance = startChance;
        this.startDelay = Math.max(0, startDelay);
        this.rollTimes = Math.max(1, rollTimes);
        this.rollTickInterval = Math.max(1, rollTickInterval);
        this.rollSlowdownEvery = rollSlowdownEvery;
        this.rollSlowdownTicks = rollSlowdownTicks;
        this.slots = slot;
        this.mode = mode;
        this.winSlots = winSlots;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public TaskStartAction getStartAction() {
        return startAction;
    }

    public double getStartChance() {
        return startChance;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public int getRollTimes() {
        return rollTimes;
    }

    public long getRollTickInterval() {
        return rollTickInterval;
    }

    public long getRollSlowdownTicks() {
        return rollSlowdownTicks;
    }

    public long getRollSlowdownEvery() {
        return rollSlowdownEvery;
    }

    public int[] getSlots() {
        return slots;
    }

    @NotNull
    public SliderInfo.Mode getSlotsMode() {
        return mode;
    }

    public int[] getWinSlots() {
        return winSlots;
    }
}
