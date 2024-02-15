package su.nightexpress.excellentcrates.opening.animation;

import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.opening.task.TaskStartAction;

import java.util.List;

public class AnimationInfo {

    private final String          id;
    private final TaskStartAction startAction;
    private final long            startDelay;
    private final int[]           slots;
    private final Mode            mode;
    private final long            tickInterval;
    private final Sound           soundTick;
    private final List<ItemStack> items;

    public enum Mode {
        SINGLE, TOGETHER
    }

    public AnimationInfo(@NotNull String id, @NotNull TaskStartAction startAction,
                         long startDelay, int[] slots, @NotNull Mode mode, long tickInterval,
                         @Nullable Sound soundTick,
                         @NotNull List<ItemStack> items) {
        this.id = id.toLowerCase();
        this.startAction = startAction;
        this.slots = slots;
        this.mode = mode;
        this.tickInterval = Math.max(0, tickInterval);
        this.startDelay = Math.max(0, startDelay);
        this.soundTick = soundTick;
        this.items = items;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public TaskStartAction getStartAction() {
        return startAction;
    }

    public long getStartDelay() {
        return startDelay;
    }

    public int[] getSlots() {
        return slots;
    }

    @NotNull
    public Mode getMode() {
        return mode;
    }

    public long getTickInterval() {
        return tickInterval;
    }

    @Nullable
    public Sound getSoundTick() {
        return soundTick;
    }

    public List<ItemStack> getItems() {
        return items;
    }
}
