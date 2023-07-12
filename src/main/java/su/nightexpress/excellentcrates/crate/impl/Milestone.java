package su.nightexpress.excellentcrates.crate.impl;

import org.jetbrains.annotations.NotNull;
import su.nexmedia.engine.api.config.JYML;
import su.nexmedia.engine.api.placeholder.Placeholder;
import su.nexmedia.engine.api.placeholder.PlaceholderMap;
import su.nexmedia.engine.utils.NumberUtil;
import su.nightexpress.excellentcrates.Placeholders;

public class Milestone implements Placeholder {

    private String rewardId;
    private int    priority;
    private int    openings;

    private final PlaceholderMap placeholderMap;

    public Milestone(@NotNull String rewardId, int priority, int openings) {
        this.setRewardId(rewardId);
        this.setPriority(priority);
        this.setOpenings(openings);

        this.placeholderMap = new PlaceholderMap()
            .add(Placeholders.MILESTONE_OPENINGS, () -> NumberUtil.format(this.getOpenings()))
            .add(Placeholders.MILESTONE_REWARD_ID, this::getRewardId);
    }

    @NotNull
    public static Milestone read(@NotNull JYML cfg, @NotNull String path) {
        String rewardId = cfg.getString(path + ".Reward_Id", "null");
        int openings = cfg.getInt(path + ".Openings");
        return new Milestone(rewardId, 0, openings);
    }

    public void write(@NotNull JYML cfg, @NotNull String path) {
        cfg.set(path + ".Reward_Id", this.getRewardId());
        cfg.set(path + ".Openings", this.getOpenings());
    }

    @Override
    @NotNull
    public PlaceholderMap getPlaceholders() {
        return this.placeholderMap;
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
