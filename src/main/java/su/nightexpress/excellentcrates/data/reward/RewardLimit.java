package su.nightexpress.excellentcrates.data.reward;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.DataManager;
import su.nightexpress.excellentcrates.crate.limit.LimitValues;
import su.nightexpress.nightcore.util.TimeUtil;

public class RewardLimit {

    private static final int INIT_AMOUNT = 0;
    private static final long INIT_RESET = -1L;

    private final String crateId;
    private final String rewardId;
    private final String holder;

    private int  amount;
    private long resetDate;

    private boolean saveRequired;

    @NotNull
    public static RewardLimit create(@NotNull Reward reward, @Nullable Player player) {
        Crate crate = reward.getCrate();
        String holder = DataManager.getHolder(reward, player);

        return new RewardLimit(crate.getId(), reward.getId(), holder, INIT_AMOUNT, INIT_RESET);
    }

    public RewardLimit(@NotNull String crateId, @NotNull String rewardId, @NotNull String holder, int amount, long resetDate) {
        this.crateId = crateId.toLowerCase();
        this.rewardId = rewardId.toLowerCase();
        this.holder = holder;
        this.setAmount(amount);
        this.setResetDate(resetDate);
    }

    public boolean isSaveRequired() {
        return this.saveRequired;
    }

    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }

    public void reset() {
        this.amount = INIT_AMOUNT;
        this.resetDate = INIT_RESET;
    }

    public boolean resetIfReady() {
        if (this.isResetTime()) {
            this.reset();
            return true;
        }
        return false;
    }

    public boolean updateResetTime(@NotNull LimitValues values) {
        //if (!values.hasResetTime()) return false;

        if (/*(this.resetDate == 0 || this.isResetTime()) && */values.isResetStep(this.amount)) {
            this.resetDate = values.generateResetTimestamp();
            return true;
        }
        return false;
    }

    public void addRoll(int amount) {
        this.amount += amount;
    }

    @NotNull
    public String getCrateId() {
        return this.crateId;
    }

    @NotNull
    public String getRewardId() {
        return this.rewardId;
    }

    @NotNull
    public String getHolder() {
        return this.holder;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public long getResetDate() {
        return this.resetDate;
    }

    public void setResetDate(long resetDate) {
        this.resetDate = resetDate;
    }

    public boolean isResetTime() {
        return /*this.resetDate > 0 && */TimeUtil.isPassed(this.resetDate);
    }
}
