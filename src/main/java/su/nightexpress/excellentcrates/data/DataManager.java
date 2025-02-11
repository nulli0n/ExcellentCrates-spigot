package su.nightexpress.excellentcrates.data;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.reward.RewardKey;
import su.nightexpress.excellentcrates.data.reward.RewardLimit;
import su.nightexpress.nightcore.manager.AbstractManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class DataManager extends AbstractManager<CratesPlugin> {

    private final Map<String, GlobalCrateData> crateDataMap;
    private final Map<RewardKey, RewardLimit>  rewardLimitMap;

    private boolean dataLoaded;

    public DataManager(@NotNull CratesPlugin plugin) {
        super(plugin);
        this.crateDataMap = new ConcurrentHashMap<>();
        this.rewardLimitMap = new ConcurrentHashMap<>();
    }

    @Override
    protected void onLoad() {
        this.plugin.runTaskAsync(() -> this.loadData());

        this.addAsyncTask(this::saveCrateDatas, Config.DATA_CRATE_DATA_SAVE_INTERVAL.get());
        this.addAsyncTask(this::saveRewardLimits, Config.DATA_REWARD_LIMITS_SAVE_INTERVAL.get());
    }

    @Override
    protected void onShutdown() {
        this.saveData();
        this.crateDataMap.clear();
        this.rewardLimitMap.clear();
        this.dataLoaded = false;
    }

    public void saveData() {
        this.saveCrateDatas();
        this.saveRewardLimits();
    }

    public void saveCrateDatas() {
        Set<GlobalCrateData> dataSet = new HashSet<>();

        this.getCrateDatas().forEach(data -> {
            if (data.isSaveRequired()) {
                dataSet.add(data);
                data.setSaveRequired(false);
            }
        });
        if (dataSet.isEmpty()) return;

        this.plugin.getDataHandler().updateCrateDatas(dataSet);
        //this.plugin.debug("Saved " + dataSet.size() + " crate datas.");
    }

    public void saveRewardLimits() {
        Set<RewardLimit> limits = new HashSet<>();

        this.getRewardLimits().forEach(limit -> {
            if (limit.isSaveRequired()) {
                limits.add(limit);
                limit.setSaveRequired(false);
            }
        });
        if (limits.isEmpty()) return;

        this.plugin.getDataHandler().updateRewardLimits(limits);
        //this.plugin.debug("Saved " + limits.size() + " reward limits.");
    }

    public void loadData() {
        this.loadCrateDatas();
        this.loadRewardLimits();

        this.dataLoaded = true;
    }

    public void loadCrateDatas() {
        this.crateDataMap.clear();

        this.plugin.getDataHandler().loadCrateDatas().forEach(data -> {
            this.crateDataMap.put(data.getCrateId(), data);
        });

        //this.plugin.debug("Loaded " + this.crateDataMap.size() + " crate datas.");
    }

    public void loadRewardLimits() {
        this.rewardLimitMap.clear();

        this.plugin.getDataHandler().loadRewardLimits().forEach(limit -> {
            if (!this.removeExpired(limit)) {
                this.addRewardLimit(limit);
            }
        });

        //this.plugin.debug("Loaded " + this.rewardLimitMap.size() + " reward limit datas.");
    }



    public void handleSynchronization() {
        if (!this.isDataLoaded()) return;

        if (Config.isCrateDataSynchronized()) {
            this.loadCrateDatas();
        }
        if (Config.isRewardLimitsSynchronized()) {
            this.loadRewardLimits();
        }
    }

    public void handleCrateRemoval(@NotNull Crate crate) {
        if (Config.isCrateDataSynchronized()) {
            this.deleteCrateData(crate);
        }
        if (Config.isRewardLimitsSynchronized()) {
            this.deleteRewardLimits(crate);
        }
    }

    public void handleRewardRemoval(@NotNull Reward reward) {
        if (Config.isRewardLimitsSynchronized()) {
            this.deleteRewardLimits(reward);
        }
    }



    public boolean isDataLoaded() {
        return this.dataLoaded;
    }

    @NotNull
    public Set<GlobalCrateData> getCrateDatas() {
        return new HashSet<>(this.crateDataMap.values());
    }

    @Nullable
    public GlobalCrateData getCrateData(@NotNull String crateId) {
        return this.crateDataMap.get(crateId.toLowerCase());
    }

    @NotNull
    public GlobalCrateData getCrateDataOrCreate(@NotNull Crate crate) {
        GlobalCrateData data = this.getCrateData(crate.getId());
        if (data != null) return data;

        GlobalCrateData fresh = GlobalCrateData.create(crate);
        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().insertCrateData(fresh));
        this.crateDataMap.put(fresh.getCrateId(), fresh);
        return fresh;
    }

    public void deleteCrateData(@NotNull Crate crate) {
        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().deleteCrateData(crate));
        this.crateDataMap.remove(crate.getId());
    }



    @NotNull
    public RewardLimit getRewardLimitOrCreate(@NotNull Reward reward, @Nullable Player player) {
        RewardLimit limit = this.getRewardLimit(reward, player);
        if (limit != null && !this.removeExpired(limit)) return limit;

        RewardLimit fresh = RewardLimit.create(reward, player);
        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().insertRewardLimit(fresh));
        this.addRewardLimit(fresh);
        return fresh;
    }

    @Nullable
    public RewardLimit getRewardLimit(@NotNull Reward reward, @Nullable Player player) {
        RewardKey key = getRewardKey(reward, player);
        return this.rewardLimitMap.get(key);
    }

    @NotNull
    public Set<RewardLimit> getRewardLimits() {
        return new HashSet<>(this.rewardLimitMap.values());
    }

    private void addRewardLimit(@NotNull RewardLimit limit) {
        RewardKey key = getRewardKey(limit);
        this.rewardLimitMap.put(key, limit);
    }

    private boolean removeExpired(@NotNull RewardLimit limit) {
        if (!limit.isResetTime()) return false;

        //this.plugin.debug("Expired reward limit removed: " + limit.getHolder() + " | " + limit.getCrateId() + " | " + limit.getRewardId());
        this.deleteRewardLimit(limit);
        return true;
    }

    public void deleteRewardLimit(@NotNull RewardLimit limit) {
        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().deleteRewardLimit(limit));
        this.rewardLimitMap.remove(getRewardKey(limit));
    }

    public void deleteRewardLimits(@NotNull Crate crate) {
        String crateId = crate.getId();

        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().deleteRewardLimits(crate));
        this.rewardLimitMap.keySet().removeIf(key -> key.getCrateId().equalsIgnoreCase(crateId));
    }

    public void deleteRewardLimits(@NotNull Reward reward) {
        String crateId = reward.getCrate().getId();
        String rewardId = reward.getId();

        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().deleteRewardLimits(reward));
        this.rewardLimitMap.keySet().removeIf(key -> key.getCrateId().equalsIgnoreCase(crateId) && key.getRewardId().equalsIgnoreCase(rewardId));
    }

    public void deleteRewardLimits(@NotNull UUID playerId) {
        String holder = playerId.toString();

        this.plugin.runTaskAsync(() -> this.plugin.getDataHandler().deleteRewardLimits(playerId));
        this.rewardLimitMap.keySet().removeIf(key -> key.getHolder().equalsIgnoreCase(holder));
    }



    @NotNull
    public static String getHolder(@NotNull Reward reward, @Nullable Player player) {
        return player == null ? reward.getCrate().getId() : player.getUniqueId().toString();
    }

    @NotNull
    public static RewardKey getRewardKey(@NotNull Reward reward, @Nullable Player player) {
        Crate crate = reward.getCrate();
        String holder = getHolder(reward, player);

        return new RewardKey(holder, crate.getId(), reward.getId());
    }

    @NotNull
    public static RewardKey getRewardKey(@NotNull RewardLimit limit) {
        return new RewardKey(limit.getHolder(), limit.getCrateId(), limit.getRewardId());
    }
}
