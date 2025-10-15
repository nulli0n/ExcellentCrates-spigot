package su.nightexpress.excellentcrates.opening.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Spinner;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerHolder;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerType;
import su.nightexpress.nightcore.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

public class InventoryOpening extends AbstractOpening {

    protected final InventoryProvider config;
    protected final InventoryView     view;
    protected final List<Spinner>    spinners;

    private boolean launched;
    private long    closeTicks;
    private long    launchTicks;

    public InventoryOpening(@NotNull CratesPlugin plugin,
                            @NotNull InventoryProvider config,
                            @NotNull InventoryView view,
                            @NotNull Player player,
                            @NotNull CrateSource source,
                            @Nullable Cost cost) {
        super(plugin, player, source, cost);
        this.view = view;
        this.config = config;
        this.spinners = new ArrayList<>();
        this.closeTicks = config.getCompletionPauseTicks();
        this.launchTicks = 0L;
    }

    @Deprecated
    public int[] parseSlots(@NotNull String str) {
        return NumberUtil.getIntArray(str);
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    public void launch() {

    }

    public void onClick(@NotNull InventoryClickEvent event) {
        Inventory clickedInv = event.getInventory();

        if ((clickedInv.getType() != InventoryType.CRAFTING && clickedInv.getType() != InventoryType.CREATIVE) || !this.isSpinnersCompleted()) {
            event.setCancelled(true);
        }
    }

    @Override
    protected void onStart() {
        this.config.getDefaultItems().values().forEach(menuItem -> {
            for (int slot : menuItem.getSlots()) {
                this.view.getTopInventory().setItem(slot, menuItem.getItem().getItemStack());
            }
        });

        //this.player.openInventory(this.view);

        this.launched = true;
        this.launchTicks = 0L;

        this.config.getSpinners().forEach(this::runSpinner);
    }

    @Override
    protected void onTick() {
        if (this.isSpinnersCompleted()) {
            if (this.closeTicks > 0) {
                this.closeTicks--;
            }
        }
        else {
            if (this.player.getOpenInventory() != this.view) {
                this.player.openInventory(this.view);
            }
        }

        this.getSpinners().forEach(Spinner::tick);
        this.launchTicks++;
    }

    @Override
    protected void onStop() {
        this.getSpinners().forEach(Spinner::stop);

        super.onStop();

        if (this.player.getOpenInventory() == this.view) {
            this.player.closeInventory();
        }
    }

    @Override
    protected void onComplete() {

    }

    @Override
    public boolean isCompleted() {
        return this.isSpinnersCompleted() && this.closeTicks <= 0;
    }

    private boolean isSpinnersCompleted() {
        return !this.spinners.isEmpty() && this.spinners.stream().allMatch(spinner -> spinner.hasSpin() && (!spinner.isRunning() || spinner.isCompleted()));
    }

    public boolean canSkip() {
        long maxTicks = this.config.getMaxTicksForSkip();
        if (maxTicks < 0) return false;

        return this.launchTicks <= maxTicks;
    }

    @Override
    public void instaRoll() {
        this.closeTicks = 0L; // Do not schedule inventory closing.
        this.setRefundable(false); // Do not return keys.

        if (!this.isLaunched()) {
            this.launch();
        }

        this.getSpinners().forEach(spinner -> {
            spinner.setSilent(true);
            spinner.tickAll();
            //System.out.println("[" + spinner.getId() + " ticks] Count: " + spinner.getCurrentSpins() + " / Total: " + spinner.getTotalSpins() + ", Done: " + spinner.isCompleted());
            spinner.stop();
            //System.out.println("still running: " + spinner.isRunning());
        });
        this.getSpinners().removeIf(spinner -> spinner.isRunning() || !spinner.hasSpin());

        this.stop();
    }


    public void runSpinner(@NotNull SpinnerHolder holder) {
        if (holder.getType() == SpinnerType.REWARD) {
            this.setRefundable(false);
        }

        Spinner spinner = holder.createSpinner(this.plugin, this);
        this.addSpinner(spinner);
    }

    public void addSpinner(@NotNull Spinner spinner) {
        this.spinners.add(spinner);
        spinner.start();
    }

    @NotNull
    public InventoryProvider getConfig() {
        return this.config;
    }

    @NotNull
    public InventoryView getView() {
        return this.view;
    }

    @NotNull
    public Inventory getInventory() {
        return this.view.getTopInventory();
    }

    @NotNull
    public List<Spinner> getSpinners() {
        return this.spinners;
    }

    public void setCloseTicks(long closeTicks) {
        this.closeTicks = closeTicks;
    }

    public boolean isLaunched() {
        return this.launched;
    }
}
