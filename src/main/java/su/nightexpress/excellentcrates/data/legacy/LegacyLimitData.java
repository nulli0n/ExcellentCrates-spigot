package su.nightexpress.excellentcrates.data.legacy;

public class LegacyLimitData {

    private final int  amount;
    private final long expireDate;

    public LegacyLimitData(int amount, long expireDate) {
        this.amount = amount;
        this.expireDate = expireDate;
    }

    public int getAmount() {
        return this.amount;
    }

    public long getExpireDate() {
        return this.expireDate;
    }
}
