package su.nightexpress.excellentcrates.data.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.database.AbstractUser;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateUser extends AbstractUser<CratesPlugin> {

    // TODO CrateData class

    private final Map<String, Integer>                     keys;
    private final Map<String, Integer>                     keysOnHold;
    private final Map<String, Long>                        openCooldowns;
    private final Map<String, Integer>                     openingsAmount;
    private final Map<String, Integer>                    milestones;
    private final Map<String, Map<String, RewardWinData>> rewardWinLimits;

    public CrateUser(@NotNull CratesPlugin plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public CrateUser(
        @NotNull CratesPlugin plugin,
        @NotNull UUID uuid,
        @NotNull String name,
        long dateCreated,
        long lastOnline,

        @NotNull Map<String, Integer> keys,
        @NotNull Map<String, Integer> keysOnHold,
        @NotNull Map<String, Long> openCooldowns,
        @NotNull Map<String, Integer> openingsAmount,
        @NotNull Map<String, Integer> milestones,
        @NotNull Map<String, Map<String, RewardWinData>> rewardWinLimits
    ) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.keys = keys;
        this.keysOnHold = keysOnHold;
        this.openCooldowns = openCooldowns;
        this.openingsAmount = openingsAmount;
        this.milestones = milestones;
        this.rewardWinLimits = rewardWinLimits;
    }

    @NotNull
    public Map<String, Long> getCrateCooldowns() {
        return this.openCooldowns;
    }

    public void setCrateCooldown(@NotNull Crate crate, long endDate) {
        this.setCrateCooldown(crate.getId(), endDate);
    }

    public void setCrateCooldown(@NotNull String id, long endDate) {
        if (endDate == 0L) {
            this.getCrateCooldowns().remove(id.toLowerCase());
        }
        else {
            this.getCrateCooldowns().put(id.toLowerCase(), endDate);
        }
    }

    public boolean isCrateOnCooldown(@NotNull Crate crate) {
        return this.isCrateOnCooldown(crate.getId());
    }

    public boolean isCrateOnCooldown(@NotNull String id) {
        return this.getCrateCooldown(id) != 0;
    }

    public long getCrateCooldown(@NotNull Crate crate) {
        return this.getCrateCooldown(crate.getId());
    }

    public long getCrateCooldown(@NotNull String id) {
        this.getCrateCooldowns().values().removeIf(endDate -> endDate >= 0 && endDate < System.currentTimeMillis());
        return this.getCrateCooldowns().getOrDefault(id.toLowerCase(), 0L);
    }

    @NotNull
    public Map<String, Integer> getOpeningsAmountMap() {
        return openingsAmount;
    }

    public int getOpeningsAmount(@NotNull Crate crate) {
        return this.getOpeningsAmount(crate.getId());
    }

    public int getOpeningsAmount(@NotNull String id) {
        return this.getOpeningsAmountMap().getOrDefault(id.toLowerCase(), 0);
    }

    public void setOpeningsAmount(@NotNull Crate crate, int amount) {
        this.setOpeningsAmount(crate.getId(), amount);
    }

    public void setOpeningsAmount(@NotNull String id, int amount) {
        this.getOpeningsAmountMap().put(id.toLowerCase(), amount);
    }

    @NotNull
    public Map<String, Integer> getMilestonesMap() {
        return this.milestones;
    }

    public int getMilestones(@NotNull Crate crate) {
        return this.getMilestones(crate.getId());
    }

    public int getMilestones(@NotNull String id) {
        return this.getMilestonesMap().getOrDefault(id.toLowerCase(), 0);
    }

    public void setMilestones(@NotNull Crate crate, int amount) {
        this.setMilestones(crate.getId(), amount);
    }

    public void setMilestones(@NotNull String id, int amount) {
        this.getMilestonesMap().put(id.toLowerCase(), amount);
    }

    @NotNull
    public Map<String, Integer> getKeysMap() {
        return this.keys;
    }

    @NotNull
    public Map<String, Integer> getKeysOnHold() {
        return this.keysOnHold;
    }

    public void addKeys(@NotNull String id, int amount) {
        this.setKeys(id, this.getKeys(id) + amount);
    }

    public void takeKeys(@NotNull String id, int amount) {
        this.addKeys(id, -amount);
    }

    public void setKeys(@NotNull String id, int amount) {
        this.getKeysMap().put(id.toLowerCase(), Math.max(0, amount));
    }

    public int getKeys(@NotNull String id) {
        return this.getKeysMap().getOrDefault(id.toLowerCase(), 0);
    }

    public void addKeysOnHold(@NotNull String id, int amount) {
        this.getKeysOnHold().put(id.toLowerCase(), Math.max(0, this.getKeysOnHold(id) + amount));
    }

    public int getKeysOnHold(@NotNull String id) {
        return this.getKeysOnHold().getOrDefault(id.toLowerCase(), 0);
    }

    public void cleanKeysOnHold() {
        this.getKeysOnHold().clear();
    }

    @NotNull
    public Map<String, Map<String, RewardWinData>> getRewardWinLimits() {
        return rewardWinLimits;
    }

    @Nullable
    public RewardWinData getWinData(@NotNull Reward reward) {
        return this.getWinData(reward.getCrate().getId(), reward.getId());
    }

    @NotNull
    public RewardWinData getRewardDataOrCreate(@NotNull Reward reward) {
        Crate crate = reward.getCrate();

        return this.rewardWinLimits.computeIfAbsent(crate.getId().toLowerCase(), k -> new HashMap<>())
            .computeIfAbsent(reward.getId().toLowerCase(), k -> RewardWinData.create());
    }

    @Nullable
    public RewardWinData getWinData(@NotNull String crateId, @NotNull String rewardId) {
        return this.getRewardWinLimits().getOrDefault(crateId.toLowerCase(), Collections.emptyMap())
            .get(rewardId.toLowerCase());
    }

    public void setRewardWinLimit(@NotNull Reward reward, @NotNull RewardWinData rewardLimit) {
        this.setRewardWinLimit(reward.getCrate().getId(), reward.getId(), rewardLimit);
    }

    public void setRewardWinLimit(@NotNull String crateId, @NotNull String rewardId, @NotNull RewardWinData rewardLimit) {
        this.getRewardWinLimits().computeIfAbsent(crateId.toLowerCase(), k -> new HashMap<>())
            .put(rewardId.toLowerCase(), rewardLimit);
    }

    public void removeRewardWinLimit(@NotNull String crateId) {
        this.getRewardWinLimits().remove(crateId.toLowerCase());
    }

    public void removeRewardWinLimit(@NotNull String crateId, @NotNull String rewardId) {
        this.getRewardWinLimits().getOrDefault(crateId.toLowerCase(), new HashMap<>())
            .remove(rewardId.toLowerCase());
    }
}
