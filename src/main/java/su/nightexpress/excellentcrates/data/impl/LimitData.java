package su.nightexpress.excellentcrates.data.impl;

import org.jetbrains.annotations.NotNull;
import su.nightexpress.excellentcrates.crate.impl.LimitValues;

public class LimitData {

    private int  amount;
    private long expireDate;

    @NotNull
    public static LimitData create() {
        return new LimitData(0, 0);
    }

    public LimitData(int amount, long expireDate) {
        this.setAmount(amount);
        this.setExpireDate(expireDate);
    }

    public void reset() {
        this.amount = 0;
        this.expireDate = 0;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getExpireDate() {
        return this.expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isExpired() {
        return this.expireDate > 0 && System.currentTimeMillis() > this.expireDate;
    }

    public boolean hasCooldown() {
        return this.expireDate != 0 && !this.isExpired();
    }

    public boolean isOutOfStock(@NotNull LimitValues values) {
        if (!values.isEnabled() || values.isUnlimitedAmount()) return false;

        return this.amount >= values.getAmount();
    }
}
