package su.nightexpress.excellentcrates.opening.inventory.spinner.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.AbstractSpinner;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinMode;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RewardSpinner extends AbstractSpinner {

    private final Set<Rarity> rarities;

    private int rewardIndex;

    public RewardSpinner(@NotNull SpinnerData data, @NotNull InventoryOpening opening, @NotNull Set<Rarity> rarities) {
        super(data, opening);
        this.rarities = rarities;
        this.rewardIndex = 0;

        this.prepareRewards();
    }

    private boolean isWinSlot(int slot) {
        return Lists.contains(this.winSlots, slot);
    }

    private void prepareRewards() {
        for (int winSlot : this.winSlots) {
            if (Lists.contains(this.slots, winSlot)) {
                this.opening.addReward(this.rollReward(false));
            }
        }
    }

    @NotNull
    private Reward rollReward(boolean visual) {
        Crate crate = this.opening.getCrate();
        Player player = this.opening.getPlayer();

        if (!visual || Config.OPENINGS_GUI_SIMULATE_REAL_CHANCES.get()) {
            Map<Rarity, Double> rarityMap = new HashMap<>();
            this.rarities.forEach(rarity -> {
                if (crate.hasRewards(player, rarity)) {
                    rarityMap.put(rarity, rarity.getWeight());
                }
            });
            if (rarityMap.isEmpty()) throw new IllegalStateException("No rewards available!");

            Rarity rarity = Rnd.getByWeight(rarityMap);
            return crate.rollReward(this.opening.getPlayer(), rarity);
        }
        else {
            List<Reward> rewards = crate.getRewards(player);
            rewards.removeIf(reward -> !this.rarities.contains(reward.getRarity()));
            if (rewards.isEmpty()) throw new IllegalStateException("No rewards available!");

            return Rnd.get(rewards);
        }
    }

    @Override
    @NotNull
    public ItemStack createItem(int slot) {
        Reward reward = this.shouldUsePredictedReward(slot) ? this.opening.getRewards().get(this.rewardIndex++) : this.rollReward(true);
        if (reward == null) return new ItemStack(Material.AIR);

        return reward.getPreviewItem();
    }

    private boolean shouldUsePredictedReward(int slot) {
        if (this.rewardIndex >= this.opening.getRewards().size()) return false;

        int spinsLeft = Math.toIntExact(this.requiredSpins - this.spinCount);
        SpinMode mode = this.data.getMode();

        if (mode == SpinMode.SYNCRHONIZED) return spinsLeft == 1;
        if (mode == SpinMode.RANDOM || mode == SpinMode.INDEPENDENT) return spinsLeft == 1 && this.isWinSlot(slot);

        if (mode == SpinMode.SEQUENTAL) {
            for (int winSlot : this.winSlots) {
                int index = Lists.indexOf(this.slots, winSlot) + 1;
                if (index > 0 && spinsLeft == index) return true;
            }
        }

        return false;
    }

    @Override
    protected void spinRandom() {
        this.spinIndependent(); // Random mode makes no sense for reward spinners. Also it's not possible to predict reward for it.
    }

    @Override
    protected void onStop() {

    }
}
