package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.nightcore.config.FileConfig;

import java.util.function.UnaryOperator;

public class Milestone {

    private final Crate crate;

    private String rewardId;
    private int    openings;

    public Milestone(@NotNull Crate crate, @NotNull String rewardId, int openings) {
        this.crate = crate;

        this.setRewardId(rewardId);
        this.setOpenings(openings);
    }

    @NotNull
    public static Milestone read(@NotNull Crate crate, @NotNull FileConfig config, @NotNull String path) {
        String rewardId = config.getString(path + ".Reward_Id", "null");
        int openings = config.getInt(path + ".Openings");

        return new Milestone(crate, rewardId, openings);
    }

    public void write(@NotNull FileConfig config, @NotNull String path) {
        config.set(path + ".Reward_Id", this.rewardId);
        config.set(path + ".Openings", this.openings);
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholders() {
        return Placeholders.MILESTONE.replacer(this);
    }

    @Nullable
    public Reward getReward() {
        return this.crate.getReward(this.rewardId);
    }

    @NotNull
    public Crate getCrate() {
        return this.crate;
    }

    @NotNull
    public String getRewardId() {
        return this.rewardId;
    }

    public void setRewardId(@NotNull String rewardId) {
        this.rewardId = rewardId;
    }

    public int getOpenings() {
        return this.openings;
    }

    public void setOpenings(int openings) {
        this.openings = openings;
    }
}
