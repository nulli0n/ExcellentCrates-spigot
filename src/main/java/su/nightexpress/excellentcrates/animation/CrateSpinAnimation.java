package su.nightexpress.excellentcrates.animation;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.api.menu.AbstractMenu;
import su.nexmedia.engine.api.menu.IMenuItem;
import su.nexmedia.engine.utils.MessageUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateAnimation;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;

import java.util.*;

public class CrateSpinAnimation extends AbstractLoadableItem<ExcellentCrates> implements ICrateAnimation {

    private Menu         menu;
    private Set<Spinner>        spinners;
    private final NamespacedKey keyRewardId;

    private static final Map<Player, Set<SpinTask>> SPIN_TASKS    = new WeakHashMap<>();
    private static final Set<Material> COLORED_PANES = new HashSet<>();

    static {
        COLORED_PANES.add(Material.BLACK_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.BLUE_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.BROWN_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.CYAN_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.GRAY_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.GREEN_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.LIME_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.MAGENTA_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.ORANGE_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.PINK_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.PURPLE_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.RED_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.WHITE_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.YELLOW_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        COLORED_PANES.add(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
    }

    public CrateSpinAnimation(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);
        this.keyRewardId = new NamespacedKey(plugin, "reward_id");

        this.spinners = new HashSet<>();
        for (String sId : cfg.getSection("Settings.Spinners")) {
            Spinner spinner = new Spinner("Settings.Spinners." + sId + ".");
            this.spinners.add(spinner);
        }

        this.menu = new Menu(plugin, "Menu.");
    }

    @Override
    public void clear() {
        if (this.menu != null) {
            this.menu.clear();
            this.menu = null;
        }
        if (this.spinners != null) {
            this.spinners.clear();
            this.spinners = null;
        }
    }

    @NotNull
    public Set<Spinner> getSpinners() {
        return spinners;
    }

    @Override
    @NotNull
    public CrateSpinAnimation.Menu getMenu() {
        return this.menu;
    }

    @Override
    public void open(@NotNull Player player, @NotNull ICrate crate) {
        this.getMenu().open(player, crate);
    }

    @Override
    public void onSave() {

    }

    enum TemplateItemType {
        NONE, RAINBOW, RAINBOW_SYNC,
    }

    class Menu extends AbstractMenu<ExcellentCrates> {

        Menu(@NotNull ExcellentCrates plugin, @NotNull String path) {
            super(plugin, CrateSpinAnimation.this.getConfig(), path);

            for (String id : cfg.getSection(path + "Content")) {
                IMenuItem menuItem = cfg.getMenuItem(path + "Content." + id + ".", TemplateItemType.class);
                this.addItem(menuItem);
            }
        }

        public void open(@NotNull Player player, @NotNull ICrate crate) {
            this.open(player, 1);
            Inventory inventory = player.getOpenInventory().getTopInventory();
            getSpinners().forEach(spinner -> spinner.runTask(player, crate, inventory));
        }

        private void stopRollTasks(@NotNull Player player) {
            Set<SpinTask> runnables = SPIN_TASKS.remove(player);
            if (runnables == null || runnables.isEmpty()) return;

            runnables.forEach(run -> {
                if (run.isCancelled()) return;
                ICrateReward reward = run.crate.rollReward(player);
                if (reward != null) reward.give(player);
                run.cancel();
            });
        }

        @Override
        public void clear() {
            new HashSet<>(SPIN_TASKS.keySet()).forEach(this::stopRollTasks);
            this.getViewers().forEach(HumanEntity::closeInventory);
            super.clear();
        }

        public void update(@NotNull Inventory inventory) {
            for (IMenuItem menuItem : this.getItemsMap().values()) {
                if (menuItem.getType() != TemplateItemType.RAINBOW && menuItem.getType() != TemplateItemType.RAINBOW_SYNC) {
                    continue;
                }

                Material colorSync = null;
                for (int slot : menuItem.getSlots()) {
                    ItemStack itemHas = inventory.getItem(slot);
                    if (itemHas == null) continue;

                    String rewardId = PDCUtil.getStringData(itemHas, keyRewardId);
                    if (rewardId != null) continue;

                    if (menuItem.getType() == TemplateItemType.RAINBOW) {
                        Material color = Rnd.get(COLORED_PANES);
                        if (color != null) itemHas.setType(color);
                    }
                    else if (menuItem.getType() == TemplateItemType.RAINBOW_SYNC) {
                        if (colorSync == null) colorSync = Rnd.get(COLORED_PANES);
                        if (colorSync != null) itemHas.setType(colorSync);
                    }
                }
            }
        }

        @Override
        public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
            this.update(inventory);
        }

        @Override
        public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

        }

        @Override
        public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
            super.onClose(player, e);

            //this.stopRollTasks(player);
        }

        @Override
        public boolean cancelClick(@NotNull SlotType slotType, int slot) {
            return true;
        }
    }

    public class Spinner {

        private final int    timeStartDelayTicks;
        private final int    timeUpdateTicks;
        private final double timeRollTicks;
        private final double timeEndTicks;
        private final double timeEndRndOffset;
        private final String rollSound;

        private final int[] rewardSlots;
        private final int[] winSlots;

        Spinner(@NotNull String path) {
            this.timeStartDelayTicks = cfg.getInt(path + "Time.Start_Delay_Ticks");
            this.timeUpdateTicks = cfg.getInt(path + "Time.Spin_Task_Ticks");
            this.timeRollTicks = cfg.getDouble(path + "Time.Roll_Every_Ticks");
            this.timeEndTicks = Math.ceil(20 * cfg.getDouble(path + "Time.Duration_Second"));
            this.timeEndRndOffset = cfg.getDouble(path + "Time.Finish_Random_Offset");

            this.rollSound = cfg.getString(path + "Roll_Sound", "");
            this.rewardSlots = cfg.getIntArray(path + "Reward_Slots");
            this.winSlots = cfg.getIntArray(path + "Win_Slots");
        }

        public int getStartDelayTicks() {
            return timeStartDelayTicks;
        }

        public int getSpinTaskTicks() {
            return this.timeUpdateTicks;
        }

        public double getRollEveryTicks() {
            return this.timeRollTicks;
        }

        public double getTaskEndTicks() {
            return this.timeEndTicks;
        }

        public double getTaskEndRandomOffset() {
            return timeEndRndOffset;
        }

        @NotNull
        public String getRollSound() {
            return this.rollSound;
        }

        public int[] getRewardSlots() {
            return this.rewardSlots;
        }

        public int[] getWinSlots() {
            return this.winSlots;
        }

        public void runTask(@NotNull Player player, @NotNull ICrate crate, @NotNull Inventory inventory) {
            SpinTask task = new SpinTask(this, player, crate, inventory);
            task.runTaskTimer(plugin, this.getStartDelayTicks(), this.getSpinTaskTicks());

            SPIN_TASKS.computeIfAbsent(player, set -> new HashSet<>()).add(task);
        }
    }

    public class SpinTask extends BukkitRunnable {

        private final Player    player;
        private final ICrate    crate;
        private final Inventory inventory;
        private final Spinner   spinner;

        private       int    tickCounter;
        private final double tickMaximum;

        public SpinTask(
            @NotNull Spinner spinner,
            @NotNull Player player,
            @NotNull ICrate crate,
            @NotNull Inventory inventory) {

            this.player = player;
            this.crate = crate;
            this.inventory = inventory;
            this.spinner = spinner;

            double rndOffset = Math.ceil((Rnd.getDouble(0, this.getSpinner().getTaskEndRandomOffset()) * 20));
            this.tickMaximum = (int) this.getSpinner().getTaskEndTicks() + rndOffset;
            this.tickCounter = 0;
        }

        @NotNull
        public Spinner getSpinner() {
            return spinner;
        }

        public double getTickMaximum() {
            return tickMaximum;
        }

        @Override
        public void run() {
            if (this.player == null) {
                this.cancel();
                return;
            }

            this.tickCounter++;

            // One second pause before reward give.
            if (this.tickCounter > this.getTickMaximum() - 20) {
                if (this.tickCounter >= this.getTickMaximum()) {
                    this.cancel();
                    this.finish(false);
                }
                return;
            }

            if (this.tickCounter > this.getTickMaximum() * 0.66) {
                if (this.tickCounter % 7 != 0) {
                    return;
                }
            }
            else if (this.tickCounter > this.getTickMaximum() * 0.55) {
                if (this.tickCounter % 5 != 0) {
                    return;
                }
            }
            else if (this.tickCounter > this.getTickMaximum() * 0.33) {
                if (this.tickCounter % 3 != 0) {
                    return;
                }
            }

            this.spinReward();
        }

        private void spinReward() {
            menu.update(this.inventory);

            if (this.tickCounter % this.getSpinner().getRollEveryTicks() != 0) {
                return;
            }
            MessageUtil.sound(this.player, this.getSpinner().getRollSound());

            ICrateReward reward = this.crate.rollReward(this.player);
            if (reward == null) return;

            ItemStack preview = reward.getPreview();
            PDCUtil.setData(preview, keyRewardId, reward.getId());

            if (this.getSpinner().getRewardSlots().length == 1) {
                int slot = this.getSpinner().getRewardSlots()[0];
                this.inventory.setItem(slot, preview);
            }
            else {
                for (int index = this.getSpinner().getRewardSlots().length - 1; index > -1; index--) {
                    int slot = this.getSpinner().getRewardSlots()[index];
                    if (index == 0) {
                        this.inventory.setItem(slot, preview);
                    }
                    else {
                        int slot2 = this.getSpinner().getRewardSlots()[index-1];
                        this.inventory.setItem(slot, this.inventory.getItem(slot2));
                    }
                }
            }

            if (this.inventory.getViewers().isEmpty()) {
                this.player.openInventory(this.inventory);
            }
        }

        public void finish(boolean force) {
            if (this.player == null) return;

            Set<SpinTask> spinTasks = SPIN_TASKS.get(this.player);
            if (spinTasks == null) return;

            boolean isAllStopped = spinTasks.stream().allMatch(SpinTask::isCancelled);
            if (!force && !isAllStopped) return;

            SPIN_TASKS.remove(this.player);

            for (SpinTask task : spinTasks) {

                if (!task.isCancelled()) {
                    task.cancel();
                    for (int winSlot : task.getSpinner().getWinSlots()) {
                        ICrateReward reward = crate.rollReward(player);
                        if (reward != null) reward.give(player);
                    }
                    continue;
                }

                for (int winSlot : task.getSpinner().getWinSlots()) {
                    ItemStack win = this.inventory.getItem(winSlot);
                    if (win == null) continue;

                    String id = PDCUtil.getStringData(win, keyRewardId);
                    if (id == null) continue;

                    ICrateReward reward = this.crate.getReward(id);
                    if (reward == null) continue;

                    reward.give(player);

                    this.inventory.setItem(winSlot, null); // Clear reward to avoid duplication.
                }
            }
            this.player.closeInventory();
        }
    }
}
