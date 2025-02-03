package su.nightexpress.excellentcrates.data.reward;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class RewardKey {

    private final String holder;
    private final String crateId;
    private final String rewardId;

    public RewardKey(@NotNull String holder, @NotNull String crateId, @NotNull String rewardId) {
        this.holder = holder;
        this.crateId = crateId;
        this.rewardId = rewardId;
    }

    @NotNull
    public String getHolder() {
        return holder;
    }

    @NotNull
    public String getCrateId() {
        return crateId;
    }

    @NotNull
    public String getRewardId() {
        return rewardId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RewardKey rewardKey)) return false;
        return Objects.equals(holder, rewardKey.holder) && Objects.equals(crateId, rewardKey.crateId) && Objects.equals(rewardId, rewardKey.rewardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holder, crateId, rewardId);
    }
}
