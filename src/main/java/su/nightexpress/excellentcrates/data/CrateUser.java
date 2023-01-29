package su.nightexpress.excellentcrates.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nexmedia.engine.api.data.AbstractUser;
import su.nexmedia.engine.utils.TimeUtil;
import su.nightexpress.excellentcrates.ExcellentCrates;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.crate.Crate;
import su.nightexpress.excellentcrates.crate.CrateReward;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.UnaryOperator;

public class CrateUser extends AbstractUser<ExcellentCrates> {

    private final Map<String, Integer>                         keys;
    private final Map<String, Integer>                         keysOnHold;
    private final Map<String, Long>                            openCooldowns;
    private final Map<String, Integer> openingsAmount;
    private final Map<String, Map<String, UserRewardWinLimit>> rewardWinLimits;

    public CrateUser(@NotNull ExcellentCrates plugin, @NotNull UUID uuid, @NotNull String name) {
        this(plugin, uuid, name, System.currentTimeMillis(), System.currentTimeMillis(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
    }

    public CrateUser(
        @NotNull ExcellentCrates plugin,
        @NotNull UUID uuid,
        @NotNull String name,
        long dateCreated,
        long lastOnline,

        @NotNull Map<String, Integer> keys,
        @NotNull Map<String, Integer> keysOnHold,
        @NotNull Map<String, Long> openCooldowns,
        @NotNull Map<String, Integer> openingsAmount,
        @NotNull Map<String, Map<String, UserRewardWinLimit>> rewardWinLimits
    ) {
        super(plugin, uuid, name, dateCreated, lastOnline);
        this.keys = keys;
        this.keysOnHold = keysOnHold;
        this.openCooldowns = openCooldowns;
        this.openingsAmount = openingsAmount;
        this.rewardWinLimits = rewardWinLimits;
    }

    @NotNull
    public UnaryOperator<String> replacePlaceholers(@NotNull CrateReward reward) {
        UserRewardWinLimit rewardLimit = this.getRewardWinLimit(reward);

        int amountLeft = rewardLimit == null ? reward.getWinLimitAmount() : reward.getWinLimitAmount() - rewardLimit.getAmount();
        long expireIn = rewardLimit == null ? 0L : rewardLimit.getExpireDate();

        return str -> str
            .replace(Placeholders.USER_REWARD_WIN_LIMIT_AMOUNT_LEFT, String.valueOf(amountLeft))
            .replace(Placeholders.USER_REWARD_WIN_LIMIT_EXPIRE_IN, TimeUtil.formatTimeLeft(expireIn))
            ;
    }

    @NotNull
    public Map<String, Long> getCrateCooldowns() {
        return this.openCooldowns;
    }

    public void setCrateCooldown(@NotNull Crate crate, long endDate) {
        this.setCrateCooldown(crate.getId(), endDate);
    }

    public void setCrateCooldown(@NotNull String id, long endDate) {
        this.getCrateCooldowns().put(id.toLowerCase(), endDate);
        this.saveData(this.plugin);
    }

    public boolean isCrateOnCooldown(@NotNull Crate crate) {
        return this.getCrateCooldown(crate.getId()) != 0;
    }

    public boolean isCrateOnCooldown(@NotNull String id) {
        return this.getCrateCooldown(id) != 0;
    }

    public long getCrateCooldown(@NotNull Crate crate) {
        return this.getCrateCooldown(crate.getId());
    }

    public long getCrateCooldown(@NotNull String id) {
        this.getCrateCooldowns().values().removeIf(endDate -> endDate >= 0 && endDate < System.currentTimeMillis());
        return this.getCrateCooldowns().getOrDefault(id.toLowerCase(), 0L);
    }

    @NotNull
    public Map<String, Integer> getOpeningsAmountMap() {
        return openingsAmount;
    }

    public int getOpeningsAmount(@NotNull Crate crate) {
        return this.getOpeningsAmount(crate.getId());
    }

    public int getOpeningsAmount(@NotNull String id) {
        return this.getOpeningsAmountMap().getOrDefault(id.toLowerCase(), 0);
    }

    public void setOpeningsAmount(@NotNull Crate crate, int amount) {
        this.setOpeningsAmount(crate.getId(), amount);
    }

    public void setOpeningsAmount(@NotNull String id, int amount) {
        this.getOpeningsAmountMap().put(id.toLowerCase(), amount);
    }

    @NotNull
    public Map<String, Integer> getKeysMap() {
        return this.keys;
    }

    @NotNull
    public Map<String, Integer> getKeysOnHold() {
        return this.keysOnHold;
    }

    public void addKeys(@NotNull String id, int amount) {
        this.setKeys(id, this.getKeys(id) + amount);
    }

    public void takeKeys(@NotNull String id, int amount) {
        this.addKeys(id, -amount);
    }

    public void setKeys(@NotNull String id, int amount) {
        this.getKeysMap().put(id.toLowerCase(), Math.max(0, amount));
        this.saveData(this.plugin);
    }

    public int getKeys(@NotNull String id) {
        return this.getKeysMap().getOrDefault(id.toLowerCase(), 0);
    }

    public void addKeysOnHold(@NotNull String id, int amount) {
        this.getKeysOnHold().put(id.toLowerCase(), Math.max(0, this.getKeysOnHold(id) + amount));
        this.saveData(this.plugin);
    }

    public int getKeysOnHold(@NotNull String id) {
        return this.getKeysOnHold().getOrDefault(id.toLowerCase(), 0);
    }

    public void cleanKeysOnHold() {
        this.getKeysOnHold().clear();
        this.saveData(this.plugin);
    }

    @NotNull
    public Map<String, Map<String, UserRewardWinLimit>> getRewardWinLimits() {
        return rewardWinLimits;
    }

    @Nullable
    public UserRewardWinLimit getRewardWinLimit(@NotNull CrateReward reward) {
        return this.getRewardWinLimit(reward.getCrate().getId(), reward.getId());
    }

    @Nullable
    public UserRewardWinLimit getRewardWinLimit(@NotNull String crateId, @NotNull String rewardId) {
        return this.getRewardWinLimits().getOrDefault(crateId.toLowerCase(), Collections.emptyMap())
            .get(rewardId.toLowerCase());
    }

    public void setRewardWinLimit(@NotNull CrateReward reward, @NotNull UserRewardWinLimit rewardLimit) {
        this.setRewardWinLimit(reward.getCrate().getId(), reward.getId(), rewardLimit);
    }

    public void setRewardWinLimit(@NotNull String crateId, @NotNull String rewardId, @NotNull UserRewardWinLimit rewardLimit) {
        this.getRewardWinLimits().computeIfAbsent(crateId.toLowerCase(), k -> new HashMap<>())
            .put(rewardId.toLowerCase(), rewardLimit);
        this.saveData(this.plugin);
    }

    public void removeRewardWinLimit(@NotNull String crateId) {
        this.getRewardWinLimits().remove(crateId.toLowerCase());
        this.saveData(this.plugin);
    }

    public void removeRewardWinLimit(@NotNull String crateId, @NotNull String rewardId) {
        this.getRewardWinLimits().getOrDefault(crateId.toLowerCase(), new HashMap<>())
            .remove(rewardId.toLowerCase());
        this.saveData(this.plugin);
    }
}
