package su.nightexpress.excellentcrates.data.crate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.Crate;

import java.util.UUID;

public class GlobalCrateData {

    private final String crateId;

    private UUID latestOpenerId;
    private String latestOpenerName;
    private String latestRewardId;

    private boolean saveRequired;

    @NotNull
    public static GlobalCrateData create(@NotNull Crate crate) {
        return new GlobalCrateData(crate.getId(), null, null, null);
    }

    public GlobalCrateData(@NotNull String crateId, @Nullable UUID latestOpenerId, @Nullable String latestOpenerName, @Nullable String latestRewardId) {
        this.crateId = crateId.toLowerCase();
        this.latestOpenerId = latestOpenerId;
        this.latestOpenerName = latestOpenerName;
        this.latestRewardId = latestRewardId;
    }

    public boolean isSaveRequired() {
        return this.saveRequired;
    }

    public void setSaveRequired(boolean saveRequired) {
        this.saveRequired = saveRequired;
    }

    @Nullable
    public String getLatestOpener() {
        if (this.latestOpenerId == null) return null;

        Player player = Bukkit.getPlayer(this.latestOpenerId);
        if (player == null) return this.latestOpenerName;

        return player.getDisplayName();
    }

    public void setLatestOpener(@NotNull Player player) {
        this.latestOpenerId = player.getUniqueId();
        this.latestOpenerName = player.getName();
    }

    public void setLatestReward(@NotNull Reward reward) {
        this.latestRewardId = reward.getId();
    }

    @NotNull
    public String getCrateId() {
        return this.crateId;
    }

    @Nullable
    public UUID getLatestOpenerId() {
        return this.latestOpenerId;
    }

    @Nullable
    public String getLatestOpenerName() {
        return this.latestOpenerName;
    }

    @Nullable
    public String getLatestRewardId() {
        return this.latestRewardId;
    }
}
