package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.nightcore.config.FileConfig;
import su.nightexpress.nightcore.util.placeholder.Placeholder;
import su.nightexpress.nightcore.util.placeholder.PlaceholderMap;

public class Milestone implements Placeholder {

    private final Crate crate;
    private final PlaceholderMap placeholderMap;

    private String rewardId;
    private int priority;
    private int openings;

    public Milestone(@NotNull Crate crate, @NotNull String rewardId, int priority, int openings) {
        this.crate = crate;
        this.placeholderMap = Placeholders.forMilestone(this);

        this.setRewardId(rewardId);
        this.setPriority(priority);
        this.setOpenings(openings);
    }

    @NotNull
    public static Milestone read(@NotNull Crate crate, @NotNull FileConfig config, @NotNull String path) {
        String rewardId = config.getString(path + ".Reward_Id", "null");
        int openings = config.getInt(path + ".Openings");

        return new Milestone(crate, rewardId, 0, openings);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Reward_Id", this.getRewardId());
        config.set(path + ".Openings", this.getOpenings());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
    }

    @Nullable
    public Reward getReward() {
        return this.crate.getReward(this.getRewardId());
    }

    @NotNull
    public Crate getCrate() {
        return crate;
    }

    @NotNull
    public String getRewardId() {
        return rewardId;
    }

    public void setRewardId(@NotNull String rewardId) {
        this.rewardId = rewardId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOpenings() {
        return openings;
    }

    public void setOpenings(int openings) {
        this.openings = openings;
    }
}
