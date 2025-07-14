package su.nightexpress.excellentcrates.opening.inventory.spinner;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.opening.Spinner;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.nightcore.bridge.wrap.NightSound;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public abstract class AbstractSpinner implements Spinner {

    protected final SpinnerData      data;
    protected final InventoryOpening opening;
    protected final Inventory        inventory;
    protected final int[]            slots;
    protected final int[]            winSlots;

    protected boolean silent;
    protected boolean running;
    protected long    tickInterval;
    protected long    tickCount;

    protected List<SpinStep> steps;
    protected SpinStep       currentStep;
    protected long           stepCount;

    protected int  requiredSpins;
    protected long spinCount;
    protected long spinDelay;

    public AbstractSpinner(@NotNull SpinnerData data, @NotNull InventoryOpening opening) {
        this.data = data;
        this.opening = opening;
        this.inventory = opening.getInventory();

        this.slots = data.getSlots();
        this.winSlots = opening.getConfig().getWinSlots();

        this.steps = new ArrayList<>(data.getSpinSteps());
        this.requiredSpins = this.steps.stream().mapToInt(SpinStep::getSpinsAmount).sum();

        this.spinCount = 0;
        this.spinDelay = data.getSpinDelay();
    }

    private void nextStep() {
        if (this.steps.isEmpty()) {
            this.currentStep = null;
            return;
        }

        this.currentStep = this.steps.removeFirst();
        this.tickInterval = this.currentStep.getTickInterval();
        this.stepCount = 0L;
        this.tickCount = 0L;
    }

    private boolean isStepDone() {
        return this.stepCount >= this.currentStep.getSpinsAmount();
    }

    @Override
    public void start() {
        if (this.running) return;

        this.running = true;
        this.nextStep();
    }

    @Override
    public void stop() {
        if (!this.running) return;

        this.running = false;
        this.onStop();
    }

    @Override
    public void tick() {
        if (!this.running) return;

        if (this.isCompleted()) {
            this.stop();
            return;
        }

        if (this.isSpinTime()) {
            this.onSpin();
        }

        this.tickCount = Math.max(0L, this.tickCount + 1L);
    }

    @Override
    public void tickAll() {
        if (!this.running) return;

        long total = Math.max(0L, this.getTotalSpins());

        for (int count = 0; count < total; count++) {
            if (this.isCompleted()) break;

            this.onSpin();
        }
    }

    @Override
    public boolean isSpinTime() {
        if (this.spinDelay > 0) {
            this.spinDelay--;
            return false;
        }

        return this.tickCount == 0 || this.tickCount % this.tickInterval == 0L;
    }

    protected abstract void onStop();

    protected void onSpin() {
        if (!this.isSilent()) {
            NightSound sound = this.data.getSound();
            if (sound != null) sound.play(this.opening.getPlayer());
        }

        switch (this.data.getMode()) {
            case SEQUENTAL -> this.spinSequental();
            case INDEPENDENT -> this.spinIndependent();
            case SYNCRHONIZED -> this.spinSynchronized();
            case RANDOM -> this.spinRandom();
        }

        this.stepCount++;
        this.spinCount++;

        if (this.isStepDone()) {
            this.nextStep();
        }
    }

    @NotNull
    public abstract ItemStack createItem(int slot);

    private boolean isOutOfBounds(int slot) {
        return slot < 0 || slot >= this.inventory.getSize();
    }

    protected void spinSequental() {
        ItemStack item = this.createItem(-1);

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

    protected void spinIndependent() {
        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            ItemStack item = this.createItem(slot);

            this.inventory.setItem(slot, item);
        }
    }

    protected void spinSynchronized() {
        ItemStack item = this.createItem(-1);

        for (int slot : this.slots) {
            if (this.isOutOfBounds(slot)) continue;

            this.inventory.setItem(slot, item);
        }
    }

    protected void spinRandom() {
        List<Integer> slots = new ArrayList<>(IntStream.of(this.slots).boxed().toList());
        int roll = Rnd.get(slots.size() + 1);
        if (roll <= 0) return;

        while (roll > 0 && !slots.isEmpty()) {
            int slot = slots.remove(Rnd.get(slots.size()));

            if (!this.isOutOfBounds(slot)) {
                ItemStack item = this.createItem(slot);
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
        return this.currentStep == null;
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
        return this.requiredSpins;
    }

    @Override
    public long getStepCount() {
        return this.stepCount;
    }

    @Override
    public void setStepCount(long spins) {
        this.stepCount = Math.max(0, spins);
    }

    @Override
    public boolean hasSpin() {
        return this.spinCount > 0L;
    }
}
