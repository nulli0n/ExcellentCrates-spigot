package su.nightexpress.excellentcrates.data.crate;

import su.nightexpress.nightcore.util.TimeUtil;

public class UserCrateData {

    private long openCooldown;
    private int openings;
    private int milestone;

    public UserCrateData() {
        this(0, 0, 0);
    }

    public UserCrateData(long openCooldown, int openings, int milestone) {
        this.openCooldown = openCooldown;
        this.openings = openings;
        this.milestone = milestone;
    }

    public void removeCooldown() {
        this.setOpenCooldown(0L);
    }

    public void setCooldown(long seconds) {
        this.setOpenCooldown(TimeUtil.createFutureTimestamp(seconds));
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
}
