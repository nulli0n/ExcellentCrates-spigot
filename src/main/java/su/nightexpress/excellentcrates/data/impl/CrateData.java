package su.nightexpress.excellentcrates.data.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.util.CrateUtils;

import java.util.HashMap;
import java.util.Map;

public class CrateData {

    private final Map<String, LimitData> rewardDataMap;

    private long openCooldown;
    private int openings;
    private int milestone;

    public CrateData() {
        this(0, 0, 0, new HashMap<>());
    }

    public CrateData(long openCooldown, int openings, int milestone, @NotNull Map<String, LimitData> rewardDataMap) {
        this.openCooldown = openCooldown;
        this.openings = openings;
        this.milestone = milestone;
        this.rewardDataMap = rewardDataMap;
    }

    public void removeCooldown() {
        this.setOpenCooldown(0L);
    }

    public void setCooldown(long seconds) {
        this.setOpenCooldown(CrateUtils.createTimestamp(seconds));
    }

    public boolean hasCooldown() {
        return this.openCooldown != 0 && !this.isCooldownExpired();
    }

    public boolean isCooldownPermanent() {
        return this.openCooldown < 0;
    }

    public boolean isCooldownExpired() {
        return this.openCooldown > 0 && System.currentTimeMillis() > this.openCooldown;
    }

    public void addOpenings(int amount) {
        this.setOpenings(this.openings + Math.abs(amount));
    }

    public void addMilestones(int amount) {
        this.setMilestone(this.milestone + Math.abs(amount));
    }

    @NotNull
    public LimitData getRewardLimitOrCreate(@NotNull Reward reward) {
        return this.rewardDataMap.computeIfAbsent(reward.getId().toLowerCase(), k -> LimitData.create());
    }

    @Nullable
    public LimitData getRewardLimit(@NotNull Reward reward) {
        return this.getRewardLimit(reward.getId());
    }

    @Nullable
    public LimitData getRewardLimit(@NotNull String rewardId) {
        return this.rewardDataMap.get(rewardId.toLowerCase());
    }

    public void removeRewardLimit(@NotNull String rewardId) {
        this.rewardDataMap.remove(rewardId.toLowerCase());
    }




    public long getOpenCooldown() {
        return this.openCooldown;
    }

    public void setOpenCooldown(long openCooldown) {
        this.openCooldown = openCooldown;
    }

    public int getOpenings() {
        return openings;
    }

    public void setOpenings(int openings) {
        this.openings = Math.max(0, openings);
    }

    public int getMilestone() {
        return milestone;
    }

    public void setMilestone(int milestone) {
        this.milestone = Math.max(0, milestone);
    }

    @NotNull
    public Map<String, LimitData> getRewardDataMap() {
        return rewardDataMap;
    }
}
