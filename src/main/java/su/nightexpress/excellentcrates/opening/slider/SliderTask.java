package su.nightexpress.excellentcrates.opening.slider;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.utils.MessageUtil;
import su.nexmedia.engine.utils.PDCUtil;
import su.nexmedia.engine.utils.random.Rnd;
import su.nightexpress.excellentcrates.ExcellentCratesAPI;
import su.nightexpress.excellentcrates.Keys;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.CrateReward;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;
import su.nightexpress.excellentcrates.opening.task.OpeningTask;

public class SliderTask extends OpeningTask {

    private final SliderInfo parent;

    private long ticksSinceStart;
    private int  rolls;
    private long rollSpeed;

    public SliderTask(@NotNull PlayerOpeningData data, @NotNull SliderInfo parent) {
        super(data);
        this.parent = parent;
    }

    @Override
    protected boolean onStart() {
        this.ticksSinceStart = 0;
        this.rolls = 0;
        this.rollSpeed = this.getParent().getRollTickInterval();

        this.runTaskTimer(ExcellentCratesAPI.PLUGIN, this.getParent().getStartDelay(), 1L);
        return true;
    }

    @Override
    public boolean canSkip() {
        return this.rolls < (Math.max(1, this.getParent().getRollTimes() - 3));
    }

    @Override
    protected boolean onStop(boolean force) {
        if (!force) {
            if (!this.canSkip()) return false;
        }

        if (Rnd.chance(this.getParent().getStartChance())) {
            for (int count = 0; count < this.getParent().getWinSlots().length; count++) {
                CrateReward reward = this.data.getCrate().rollReward(this.data.getPlayer());
                if (reward != null) {
                    reward.give(this.data.getPlayer());

                    CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, this.getData().getPlayer());
                    ExcellentCratesAPI.PLUGIN.getPluginManager().callEvent(rewardEvent);
                }
            }
        }
        return this.isStarted();
    }

    @Override
    public void run() {
        if (this.rolls == 0) {
            if (!Rnd.chance(this.getParent().getStartChance())) {
                this.cancel();
                this.maybeClose();
                return;
            }
        }

        if (!this.data.getPlayer().getOpenInventory().getTopInventory().equals(this.data.getInventory())) {
            this.data.getPlayer().openInventory(this.data.getInventory());
        }

        if (++this.ticksSinceStart % Math.max(1, this.rollSpeed) != 0) {
            return;
        }

        int[] slots = this.getParent().getSlots();

        if (this.getParent().getSlotsMode() == SliderInfo.Mode.INHERITANCE) {
            CrateReward reward = this.data.getCrate().rollReward(this.data.getPlayer());
            if (reward == null) return;

            ItemStack preview = reward.getPreview();
            PDCUtil.set(preview, Keys.REWARD_ID, reward.getId());

            for (int index = slots.length - 1; index > -1; index--) {
                int slot = slots[index];
                if (index == 0) {
                    this.data.getInventory().setItem(slot, preview);
                }
                else {
                    int slot2 = slots[index - 1];
                    this.data.getInventory().setItem(slot, this.data.getInventory().getItem(slot2));
                }
            }
        }
        else {
            for (int slot : slots) {
                CrateReward reward = this.data.getCrate().rollReward(this.data.getPlayer());
                if (reward == null) return;

                ItemStack preview = reward.getPreview();
                PDCUtil.set(preview, Keys.REWARD_ID, reward.getId());

                this.data.getInventory().setItem(slot, preview);
            }
        }

        this.getData().getInventory().getViewers().forEach(player -> MessageUtil.sound((Player) player, this.parent.getSoundTick()));

        if (++this.rolls >= this.getParent().getRollTimes()) {
            this.cancel();
            this.checkRewards();
            this.maybeClose();
            return;
        }
        if (this.parent.getRollSlowdownEvery() > 0) {
            if (this.rolls >= this.getParent().getRollSlowdownEvery() && this.rolls % this.getParent().getRollSlowdownEvery() == 0) {
                this.rollSpeed += this.getParent().getRollSlowdownTicks();
            }
        }
    }

    private void maybeClose() {
        if (!this.getData().isCompleted()) return;

        ExcellentCratesAPI.PLUGIN.runTaskLater(task -> {
            Inventory opened = this.getData().getPlayer().getOpenInventory().getTopInventory();
            if (this.getData().getInventory().equals(opened)) {
                this.getData().getPlayer().closeInventory();
            }
        }, Config.CRATE_OPENING_CLOSE_TIME.get());
    }

    private void checkRewards() {
        for (int slot : this.getParent().getWinSlots()) {
            ItemStack item = this.data.getInventory().getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            String rewardId = PDCUtil.getString(item, Keys.REWARD_ID).orElse(null);
            if (rewardId == null) continue;

            CrateReward reward = this.data.getCrate().getReward(rewardId);
            if (reward == null) continue;

            reward.give(this.data.getPlayer());

            CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, this.getData().getPlayer());
            ExcellentCratesAPI.PLUGIN.getPluginManager().callEvent(rewardEvent);
        }
    }

    @NotNull
    public SliderInfo getParent() {
        return parent;
    }
}
