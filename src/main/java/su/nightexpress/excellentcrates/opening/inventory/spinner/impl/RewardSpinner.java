package su.nightexpress.excellentcrates.opening.inventory.spinner.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.config.Keys;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Rarity;
import su.nightexpress.excellentcrates.opening.inventory.InventoryOpening;
import su.nightexpress.excellentcrates.opening.inventory.spinner.AbstractSpinner;
import su.nightexpress.excellentcrates.opening.inventory.spinner.SpinnerData;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.util.CrateUtils;
import su.nightexpress.nightcore.util.Lists;
import su.nightexpress.nightcore.util.PDCUtil;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RewardSpinner extends AbstractSpinner {

    private final Set<Rarity> rarities;

    public RewardSpinner(@NotNull SpinnerData data, @NotNull InventoryOpening opening, @NotNull Set<Rarity> rarities) {
        super(data, opening);
        this.rarities = rarities;
    }

    @Override
    @NotNull
    public ItemStack createItem() {
        Crate crate = this.opening.getCrate();
        Player player = this.opening.getPlayer();

        Map<Rarity, Double> rarityMap = new HashMap<>();
        this.rarities.forEach(rarity -> {
            if (crate.hasRewards(player, rarity)) {
                rarityMap.put(rarity, rarity.getWeight());
            }
        });
        if (rarityMap.isEmpty()) {
            return new ItemStack(Material.AIR);
        }

        Rarity rarity = Rnd.getByWeight(rarityMap);
        Reward reward = this.opening.getCrate().rollReward(this.opening.getPlayer(), rarity);

        ItemStack item = reward.getPreviewItem();
        PDCUtil.set(item, Keys.rewardId, reward.getId());

        return item;
    }

    @Override
    protected void onStop() {
        if (this.isCompleted()) {
            this.checkRewards();
        }
    }

    private void checkRewards() {
        for (int slot : this.opening.getConfig().getWinSlots()) {
            if (!Lists.contains(this.slots, slot)) continue;

            ItemStack item = this.opening.getView().getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            String rewardId = PDCUtil.getString(item, Keys.rewardId).orElse(null);
            if (rewardId == null) continue;

            Reward reward = this.opening.getCrate().getReward(rewardId);
            if (reward == null) continue;

            reward.give(this.opening.getPlayer());

            CrateUtils.callRewardObtainEvent(this.opening.getPlayer(), reward);

            PDCUtil.remove(item, Keys.rewardId);
        }
    }
}
