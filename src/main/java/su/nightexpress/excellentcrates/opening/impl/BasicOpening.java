package su.nightexpress.excellentcrates.opening.impl;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Lidded;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.event.CrateObtainRewardEvent;
import su.nightexpress.excellentcrates.config.Config;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.crate.impl.Reward;
import su.nightexpress.excellentcrates.hologram.HologramHandler;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.AbstractOpening;
import su.nightexpress.nightcore.util.LocationUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BasicOpening extends AbstractOpening {

    private static final Map<Block, Long> CONTAINER_CLOSE_TIME = new HashMap<>();
    private static final Map<UUID, Long> REWARD_DISAPPEAR_TIME = new HashMap<>();

    private boolean rolled;
    private boolean visuals;

    public BasicOpening(@NotNull CratesPlugin plugin, @NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        super(plugin, player, source, key);
    }

    public static void addContainerSchedule(@NotNull Block block, int seconds) {
        CONTAINER_CLOSE_TIME.put(block, System.currentTimeMillis() + seconds * 1000L);
    }

    public static void addRewardSchedule(@NotNull Player player, int seconds) {
        REWARD_DISAPPEAR_TIME.put(player.getUniqueId(), System.currentTimeMillis() + seconds * 1000L);
    }

    public static void clearVisuals(@NotNull CratesPlugin plugin) {
        CONTAINER_CLOSE_TIME.clear();

        REWARD_DISAPPEAR_TIME.forEach((uuid, date) -> {
            HologramHandler hologramHandler = plugin.getHologramHandler();
            if (hologramHandler == null) return;

            Player player = plugin.getServer().getPlayer(uuid);
            if (player == null) return;

            hologramHandler.removeReward(player);
        });
        REWARD_DISAPPEAR_TIME.clear();
    }

    public static void tickVisuals(@NotNull CratesPlugin plugin) {
        CONTAINER_CLOSE_TIME.entrySet().removeIf(entry -> {
            long date = entry.getValue();
            if (System.currentTimeMillis() < date) return false;

            if (entry.getKey().getState() instanceof Lidded lidded) {
                lidded.close();
            }
            return true;
        });

        REWARD_DISAPPEAR_TIME.entrySet().removeIf(entry -> {
            HologramHandler hologramHandler = plugin.getHologramHandler();
            if (hologramHandler == null) return true;

            Player player = plugin.getServer().getPlayer(entry.getKey());
            if (player == null) return true;

            long date = entry.getValue();
            if (System.currentTimeMillis() < date) return false;

            hologramHandler.removeReward(player);
            return true;
        });
    }

    @Override
    public void instaRoll() {
        this.visuals = false;
        this.roll();
        this.stop();
    }

    @Override
    public boolean isCompleted() {
        return this.rolled;
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @Override
    protected void onLaunch() {
        this.visuals = true;
    }

    @Override
    protected void onTick() {
        super.onTick();

        if (this.isRunning()) {
            this.roll();
        }
    }

    public void roll() {
        this.setRefundable(false);
        this.setHasRewardAttempts(true);

        Reward reward = this.getCrate().rollReward(this.player);
        reward.give(this.player);

        CrateObtainRewardEvent rewardEvent = new CrateObtainRewardEvent(reward, player);
        plugin.getPluginManager().callEvent(rewardEvent);

        Block block = this.getSource().getBlock();
        if (this.visuals && Config.CRATE_DISPLAY_REWARD_ABOVE_BLOCK.get() && block != null) {
            if (block.getState() instanceof Lidded lidded) {
                lidded.open();
                addContainerSchedule(block, 3);
            }

            HologramHandler hologramHandler = plugin.getHologramHandler();
            if (hologramHandler != null) {
                Location location = LocationUtil.getCenter(block.getLocation().add(0, 2, 0), false);
                hologramHandler.createReward(player, reward, location);
                addRewardSchedule(player, 3);
            }
        }

        this.rolled = true;
    }
}
