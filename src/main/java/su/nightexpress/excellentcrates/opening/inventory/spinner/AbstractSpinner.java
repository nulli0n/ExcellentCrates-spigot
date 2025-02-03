package su.nightexpress.excellentcrates.opening.inventory.spinner;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.opening.Spinner;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.util.bukkit.NightSound;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractSpinner implements Spinner {

    protected final SpinnerData      data;
    protected final InventoryOpening opening;
    protected final int[]            slots;
    protected final NightSound       sound;
    protected final Inventory inventory;

    protected boolean silent;
    protected long    spinCount;
    protected long    tickInterval;

    protected long    ticksToSkip;
    protected long    tickCount;
    protected boolean running;

    public AbstractSpinner(@NotNull SpinnerData data, @NotNull InventoryOpening opening) {
        this.data = data;
        this.opening = opening;
        this.slots = opening.parseSlots(data.getSlots());
        this.ticksToSkip = data.getTicksToSkip();
        this.sound = data.getSound() == null ? null : new NightSound(data.getSound(), null, 0.7F, 1F);
        this.inventory = opening.getInventory();
    }

    @Override
    public void start() {
        if (this.isRunning()) return;

        this.running = true;
        this.tickInterval = this.data.getTickInterval();
    }

    @Override
    public void stop() {
        if (!this.isRunning()) return;

        this.running = false;
        this.onStop();
    }

    @Override
    public void tick() {
        if (!this.isRunning()) return;

        if (this.isCompleted()) {
            this.stop();
            return;
        }

        if (this.isTickTime()) {
            this.onTick();
            this.tickCount = 0L;
        }
        this.tickCount++;
    }

    @Override
    public void tickAll() {
        if (!this.isRunning()) return;

        long total = Math.max(0L, this.getTotalSpins());

        for (int count = 0; count < total; count++) {
            if (this.isCompleted()) break;

            this.onTick();
        }
    }

    @Override
    public boolean isTickTime() {
        if (this.ticksToSkip > 0) {
            this.ticksToSkip--;
            return false;
        }

        return this.tickCount == 0 || this.tickCount % this.getTickInterval() == 0L;
    }

    protected abstract void onStop();

    protected void onSpin() {
        switch (this.data.getMode()) {
            case SEQUENTAL -> this.spinSequental();
            case INDEPENDENT -> this.spinIndependent();
            case SYNCRHONIZED -> this.spinSynchronized();
            case RANDOM -> this.spinRandom();
        }
    }

    protected void onTick() {
        //System.out.println("SpSd/TiCt/SpCt: " + this.spinSpeedTicks + " / " + this.tickCount + " / " + this.spinCount);

        if (!this.isSilent() && this.sound != null) {
            this.sound.play(this.opening.getPlayer());
        }

        this.onSpin();
        this.spinCount++;

        // Slowdown Spinner
        if (this.data.getSlowdownStep() > 0 && this.spinCount > 0) {
            if (this.spinCount % this.data.getSlowdownStep() == 0) {
                this.tickInterval += this.data.getSlowdownAmount();
            }
        }
    }

    @NotNull
    public abstract ItemStack createItem();

    private boolean isOutOfBounds(int slot) {
        return slot < 0 || slot >= this.inventory.getSize();
    }

    private void spinSequental() {
        ItemStack item = this.createItem();
        //if (item.getType().isAir()) return;

        for (int index = this.slots.length - 1; index > -1; index--) {
            int slot = slots[index];
            if (this.isOutOfBounds(slot)) continue;

            if (index == 0) {
                this.inventory.setItem(slot, item);
            }
            else {
                int previousSlot = slots[index - 1];
                this.inventory.setItem(slot, this.inventory.getItem(previousSlot));
            }
        }
    }

    private void spinIndependent() {
        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            ItemStack item = this.createItem();

            this.inventory.setItem(slot, item);
        }
    }

    private void spinSynchronized() {
        ItemStack item = this.createItem();

        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            this.inventory.setItem(slot, item);
        }
    }

    private void spinRandom() {
        List<Integer> slots = new ArrayList<>(IntStream.of(this.slots).boxed().toList());
        int roll = Rnd.get(slots.size() + 1);
        if (roll <= 0) return;

        while (roll > 0 && !slots.isEmpty()) {
            int slot = slots.remove(Rnd.get(slots.size()));

            if (!this.isOutOfBounds(slot)) {
                ItemStack item = this.createItem();
                this.inventory.setItem(slot, item);
            }

            roll--;
        }
    }

    @NotNull
    public InventoryOpening getOpening() {
        return this.opening;
    }

    @Override
    public boolean isCompleted() {
        return this.getTotalSpins() >= 0 && this.spinCount >= this.getTotalSpins();
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public long getTickCount() {
        return this.tickCount;
    }

    @Override
    public long getTickInterval() {
        return this.tickInterval;
    }

    @NotNull
    @Override
    public String getId() {
        return this.data.getSpinnerId();
    }

    @Override
    public boolean isSilent() {
        return this.silent;
    }

    @Override
    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public int getTotalSpins() {
        return this.data.getSpins();
    }

    @Override
    public long getCurrentSpins() {
        return this.spinCount;
    }

    @Override
    public void setCurrentSpins(long spins) {
        this.spinCount = Math.max(0, spins);
    }

    @Override
    public boolean hasSpin() {
        return this.spinCount > 0L;
    }
}
