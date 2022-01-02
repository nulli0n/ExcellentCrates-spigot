package su.nightexpress.excellentcrates.animation;

import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.manager.AbstractLoadableItem;
import su.nexmedia.engine.api.menu.*;
import su.nexmedia.engine.utils.MessageUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.api.crate.ICrate;
import su.nightexpress.excellentcrates.api.crate.ICrateAnimation;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;

import java.util.*;

public class CrateHiddenAnimation extends AbstractLoadableItem<ExcellentCrates> implements ICrateAnimation {

    private final Menu menu;

    private final int rewardAmount;

    private final ItemStack lockedItem;
    private final int[]     lockedSlots;

    private final boolean openAnimationEnabled;
    private       int     openAnimationInterval;
    private       int   openAnimationDuration;
    private final Sound openSound;

    public CrateHiddenAnimation(@NotNull ExcellentCrates plugin, @NotNull JYML cfg) {
        super(plugin, cfg);

        this.rewardAmount = cfg.getInt("Settings.Reward.Amount", 1);
        this.lockedItem = cfg.getItemNew("Settings.Locked.Item");
        this.lockedSlots = cfg.getIntArray("Settings.Locked.Slots");
        this.openSound = cfg.getEnum("Settings.Open.Sound", Sound.class);
        if (this.openAnimationEnabled = cfg.getBoolean("Settings.Open.Animation.Enabled")) {
            this.openAnimationInterval = cfg.getInt("Settings.Open.Animation.Interval", 3);
            this.openAnimationDuration = (int) cfg.getDouble("Settings.Open.Animation.Duration", 2) * 20;
        }

        this.menu = new Menu("Menu.");
    }

    @Override
    @NotNull
    public Menu getMenu() {
        return this.menu;
    }

    public void open(@NotNull Player player, @NotNull ICrate crate) {
        this.getMenu().open(player, crate);
    }

    @Override
    public void onSave() {

    }

    @Override
    public void clear() {
        if (this.menu != null) {
            this.menu.clear();
        }
    }

    class Menu extends AbstractMenu<ExcellentCrates> {

        private static final Map<Player, Set<BukkitRunnable>> TASKS = new HashMap<>();

        private final Map<Player, Set<Integer>> openedSlots;
        private final Map<Player, ICrate>        crateMap;

        public Menu(@NotNull String path) {
            super(CrateHiddenAnimation.this.plugin, CrateHiddenAnimation.this.cfg, path);
            this.openedSlots = new HashMap<>();
            this.crateMap = new HashMap<>();

            for (String sId : cfg.getSection(path + "Content")) {
                IMenuItem menuItem = cfg.getMenuItem(path + "Content." + sId, MenuItemType.class);
                this.addItem(menuItem);
            }
        }

        public void open(@NotNull Player player, @NotNull ICrate crate) {
            this.crateMap.put(player, crate);
            this.open(player, 1);
        }

        private void stopRollTasks(@NotNull Player player) {
            Set<BukkitRunnable> runnables = TASKS.remove(player);
            if (runnables == null || runnables.isEmpty()) return;

            ICrate crate = this.crateMap.get(player);
            runnables.forEach(run -> {
                if (run.isCancelled()) return;
                if (crate != null) {
                    ICrateReward reward = crate.rollReward(player);
                    if (reward != null) reward.give(player);
                }
                run.cancel();
            });
        }

        @Override
        public void clear() {
            new HashSet<>(TASKS.keySet()).forEach(this::stopRollTasks);
            this.getViewers().forEach(HumanEntity::closeInventory);
            this.openedSlots.clear();
            this.crateMap.clear();
            super.clear();
        }

        @Override
        public void onPrepare(@NotNull Player player, @NotNull Inventory inventory) {
            ICrate crate = this.crateMap.get(player);
            if (crate == null) return;

            IMenuClick click = (player1, type, e) -> {
                int slot = e.getRawSlot();
                Set<Integer> opened = this.openedSlots.computeIfAbsent(player1, k -> new HashSet<>());
                boolean isOpened = opened.add(slot);
                if (!isOpened || opened.size() > rewardAmount) return;

                Inventory inventory1 = e.getInventory();
                if (opened.size() == rewardAmount) {
                    for (int slotLocked : lockedSlots) {
                        if (opened.contains(slotLocked)) continue;
                        inventory1.setItem(slotLocked, null);
                    }
                }

                if (openAnimationEnabled) {
                    BukkitRunnable runnable = new BukkitRunnable() {

                        private int lifetime = 0;

                        @Override
                        public void run() {
                            ICrateReward rewardLast = rollReward(player1, crate, inventory1, slot);
                            if (rewardLast == null) return;

                            if ((lifetime += openAnimationInterval) > openAnimationDuration) {
                                rewardLast.give(player1);
                                this.cancel();

                                Set<BukkitRunnable> runnables = TASKS.getOrDefault(player1, Collections.emptySet());
                                boolean isFinish = runnables.stream().allMatch(BukkitRunnable::isCancelled) && runnables.size() == rewardAmount;
                                if (isFinish) {
                                    plugin.getServer().getScheduler().runTaskLater(plugin, c -> {
                                        player1.closeInventory();
                                    }, 40L);
                                }
                            }
                        }
                    };
                    runnable.runTaskTimer(plugin, 0L, openAnimationInterval);
                    TASKS.computeIfAbsent(player1, k -> new HashSet<>()).add(runnable);
                }
                else {
                    ICrateReward reward = this.rollReward(player1, crate, inventory1, slot);
                    if (reward != null) reward.give(player);
                }
            };

            for (int slot : lockedSlots) {
                IMenuItem menuItem = new MenuItem(new ItemStack(lockedItem), slot);
                menuItem.setClick(click);
                this.addItem(player, menuItem);
            }
        }

        @Nullable
        private ICrateReward rollReward(@NotNull Player player, @NotNull ICrate crate, @NotNull Inventory inventory, int slot) {
            ICrateReward reward = crate.rollReward(player);
            if (reward == null) return null;

            inventory.setItem(slot, reward.getPreview());

            if (openSound != null) {
                MessageUtil.sound(player, openSound);
            }

            return reward;
        }

        @Override
        public void onReady(@NotNull Player player, @NotNull Inventory inventory) {

        }

        @Override
        public void onClose(@NotNull Player player, @NotNull InventoryCloseEvent e) {
            super.onClose(player, e);

            this.stopRollTasks(player);

            ICrate crate = this.crateMap.get(player);
            if (crate == null) return;

            Set<Integer> opened = this.openedSlots.computeIfAbsent(player, k -> new HashSet<>());
            int diff = rewardAmount - opened.size();
            for (int count = 0; count < diff; count++) {
                ICrateReward reward = crate.rollReward(player);
                if (reward != null) reward.give(player);
            }

            this.openedSlots.remove(player);
            this.crateMap.remove(player);
        }

        @Override
        public boolean cancelClick(@NotNull SlotType slotType, int slot) {
            return true;
        }
    }
}
