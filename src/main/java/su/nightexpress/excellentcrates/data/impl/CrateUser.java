package su.nightexpress.excellentcrates.data.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.nightcore.database.AbstractUser;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CrateUser extends AbstractUser<CratesPlugin> {

    private final Map<String, Integer>   keys;
    private final Map<String, Integer>   keysOnHold;
    private final Map<String, CrateData> crateDataMap;

    public CrateUser(@NotNull CratesPlugin plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public CrateUser(@NotNull CratesPlugin plugin,
                     @NotNull UUID uuid,
                     @NotNull String name,
                     long dateCreated,
                     long lastOnline,
                     @NotNull Map<String, Integer> keys,
                     @NotNull Map<String, Integer> keysOnHold,
                     @NotNull Map<String, CrateData> crateDataMap) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.keys = keys;
        this.keysOnHold = keysOnHold;
        this.crateDataMap = new HashMap<>(crateDataMap);
    }

    @NotNull
    public Map<String, CrateData> getCrateDataMap() {
        return crateDataMap;
    }

    @NotNull
    public CrateData getCrateData(@NotNull Crate crate) {
        return this.getCrateData(crate.getId());
    }

    @NotNull
    public CrateData getCrateData(@NotNull String id) {
        return this.crateDataMap.computeIfAbsent(id.toLowerCase(), k -> new CrateData());
    }

    @Deprecated
    public void setCrateCooldown(@NotNull Crate crate, long endDate) {
        this.setCrateCooldown(crate.getId(), endDate);
    }

    @Deprecated
    public void setCrateCooldown(@NotNull String id, long endDate) {
        this.getCrateData(id).setOpenCooldown(endDate);
    }

    @Deprecated
    public boolean isCrateOnCooldown(@NotNull Crate crate) {
        return this.isCrateOnCooldown(crate.getId());
    }

    @Deprecated
    public boolean isCrateOnCooldown(@NotNull String id) {
        return this.getCrateData(id).hasCooldown();
    }

    @Deprecated
    public long getCrateCooldown(@NotNull Crate crate) {
        return this.getCrateCooldown(crate.getId());
    }

    @Deprecated
    public long getCrateCooldown(@NotNull String id) {
        return this.getCrateData(id).getOpenCooldown();
    }




    @Deprecated
    public int getOpeningsAmount(@NotNull Crate crate) {
        return this.getOpeningsAmount(crate.getId());
    }

    @Deprecated
    public int getOpeningsAmount(@NotNull String id) {
        return this.getCrateData(id).getOpenings();
    }

    @Deprecated
    public void setOpeningsAmount(@NotNull Crate crate, int amount) {
        this.setOpeningsAmount(crate.getId(), amount);
    }

    @Deprecated
    public void setOpeningsAmount(@NotNull String id, int amount) {
        this.getCrateData(id).setOpenings(amount);
    }

    @Deprecated
    public int getMilestones(@NotNull Crate crate) {
        return this.getMilestones(crate.getId());
    }

    @Deprecated
    public int getMilestones(@NotNull String id) {
        return this.getCrateData(id).getMilestone();
    }

    @Deprecated
    public void setMilestones(@NotNull Crate crate, int amount) {
        this.setMilestones(crate.getId(), amount);
    }

    @Deprecated
    public void setMilestones(@NotNull String id, int amount) {
        this.getCrateData(id).setMilestone(amount);
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


    @Deprecated
    @Nullable
    public LimitData getWinData(@NotNull Reward reward) {
        return this.getWinData(reward.getCrate().getId(), reward.getId());
    }

    @Deprecated
    @NotNull
    public LimitData getRewardDataOrCreate(@NotNull Reward reward) {
        return this.getCrateData(reward.getCrate()).getRewardLimitOrCreate(reward);
    }

    @Deprecated
    @Nullable
    public LimitData getWinData(@NotNull String crateId, @NotNull String rewardId) {
        return this.getCrateData(crateId).getRewardLimit(rewardId);
    }

//    @Deprecated
//    public void setRewardWinLimit(@NotNull Reward reward, @NotNull LimitData rewardLimit) {
//        this.setRewardWinLimit(reward.getCrate().getId(), reward.getId(), rewardLimit);
//    }
//
//    @Deprecated
//    public void setRewardWinLimit(@NotNull String crateId, @NotNull String rewardId, @NotNull LimitData rewardLimit) {
//        this.getCrateData(crateId).setRewardLimit(rewardId, rewardLimit);
//    }

    @Deprecated
    public void removeRewardWinLimit(@NotNull String crateId) {
        this.getCrateData(crateId).getRewardDataMap().clear();
    }

    @Deprecated
    public void removeRewardWinLimit(@NotNull String crateId, @NotNull String rewardId) {
        this.getCrateData(crateId).removeRewardLimit(rewardId);
    }
}
