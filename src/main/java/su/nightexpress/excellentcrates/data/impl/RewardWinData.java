package su.nightexpress.excellentcrates.data.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.RewardWinLimit;

public class RewardWinData {

    private int amount;
    private long expireDate;

    public RewardWinData(int amount, long expireDate) {
        this.setAmount(amount);
        this.setExpireDate(expireDate);
    }

    @NotNull
    public static RewardWinData create() {
        return new RewardWinData(0, 0);
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

    public boolean isOnCooldown() {
        return !this.isExpired();
    }

    public boolean isOut(@NotNull RewardWinLimit winLimit) {
        if (!winLimit.isEnabled() || winLimit.isUnlimitedAmount()) return false;

        return this.getAmount() >= winLimit.getAmount();
    }
}
