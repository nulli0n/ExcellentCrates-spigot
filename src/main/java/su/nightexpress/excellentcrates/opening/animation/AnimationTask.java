package su.nightexpress.excellentcrates.opening.animation;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.MessageUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;
import su.nightexpress.excellentcrates.opening.task.OpeningTask;

public class AnimationTask extends OpeningTask {

    private final AnimationInfo parent;

    public AnimationTask(@NotNull PlayerOpeningData data, @NotNull AnimationInfo parent) {
        super(data);
        this.parent = parent;
    }

    @Override
    protected boolean onStart() {
        if (this.parent.getStartDelay() > 0 && this.parent.getTickInterval() <= 0) {
            this.runTaskLater(ExcellentCratesAPI.PLUGIN, this.parent.getStartDelay());
        }
        else {
            this.runTaskTimer(ExcellentCratesAPI.PLUGIN, this.parent.getStartDelay(), this.parent.getTickInterval());
        }
        return true;
    }

    @Override
    protected boolean onStop(boolean force) {
        return this.isStarted();
    }

    @Override
    public void run() {
        if (this.data.isCompleted()) {
            this.cancel();
        }

        Inventory inventory = this.data.getInventory();
        if (this.parent.getMode() == AnimationInfo.Mode.TOGETHER) {
            ItemStack item = Rnd.get(this.parent.getItems());
            for (int slot : this.parent.getSlots()) {
                inventory.setItem(slot, item);
            }
        }
        else {
            for (int slot : this.parent.getSlots()) {
                inventory.setItem(slot, Rnd.get(this.parent.getItems()));
            }
        }

        inventory.getViewers().forEach(player -> MessageUtil.sound((Player) player, this.parent.getSoundTick()));
    }

    @NotNull
    public AnimationInfo getParent() {
        return parent;
    }
}
