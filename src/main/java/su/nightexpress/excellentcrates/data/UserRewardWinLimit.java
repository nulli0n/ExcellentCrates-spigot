package su.nightexpress.excellentcrates.data;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.api.crate.ICrateReward;

public class UserRewardWinLimit {

    private int amount;
    private long expireDate;

    public UserRewardWinLimit(int amount, long expireDate) {
        this.setAmount(amount);
        this.setExpireDate(expireDate);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isExpired() {
        return this.getExpireDate() >= 0 && System.currentTimeMillis() >= this.getExpireDate();
    }

    public boolean isDrained(@NotNull ICrateReward reward) {
        if (reward.isWinLimitedCooldown() && this.getExpireDate() < 0) return true;
        if (reward.isWinLimitedAmount() && this.getAmount() >= reward.getWinLimitAmount()) return true;
        return false;
    }
}
