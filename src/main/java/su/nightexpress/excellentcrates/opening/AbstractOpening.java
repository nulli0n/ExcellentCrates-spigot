package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.Placeholders;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.config.Lang;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.nightcore.util.Players;
import su.nightexpress.nightcore.util.placeholder.Replacer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractOpening implements Opening {

    protected final CratesPlugin plugin;
    protected final Player       player;
    protected final CrateSource  source;
    protected final Crate        crate;
    protected final Cost         cost;
    protected final List<Reward> rewards;

    protected long    tickCount;
    protected boolean running;
    protected boolean refundable;

    public AbstractOpening(@NotNull CratesPlugin plugin, @NotNull Player player, @NotNull CrateSource source, @Nullable Cost cost) {
        this.plugin = plugin;
        this.player = player;
        this.source = source;
        this.crate = source.getCrate();
        this.cost = cost;
        this.rewards = new ArrayList<>();
        this.setRefundable(true);
    }

    @Override
    public void start() {
        if (this.running) return;

        this.running = true;
        this.onStart();
    }

    @Override
    public void stop() {
        if (!this.running) return;

        this.running = false;
        this.onStop();
    }

    @Override
    public void tick() {
        if (!this.running) return;

        if (this.isCompleted()) {
            this.stop();
            return;
        }

        if (this.isTickTime()) {
            this.onTick();
        }

        this.tickCount = Math.max(0L, this.tickCount + 1L);
    }

    @Override
    public final boolean isRunning() {
        return this.running;
    }

    @Override
    public long getTickCount() {
        return this.tickCount;
    }

    @Override
    public boolean isTickTime() {
        return this.tickCount == 0 || this.tickCount % this.getInterval() == 0L;
    }

    protected abstract void onStart();

    protected abstract void onTick();

    protected abstract void onComplete();

    protected void onStop() {
        // 1. Refund Logic (Same as before)
        if (this.isRefundable()) {
            if (this.cost != null) this.cost.refundAll(this.player);
            if (this.source.getItem() != null) Players.addItem(this.player, this.crate.getItemStack());
        }

        this.plugin.getOpeningManager().removeOpening(this.getPlayer());

        if (this.isCompleted()) {
            this.onComplete();

            // Stats / Data updates (Same as before)
            CrateUser user = plugin.getUserManager().getOrFetch(player);
            UserCrateData userData = user.getCrateData(this.crate);
            GlobalCrateData globalData = plugin.getDataManager().getCrateDataOrCreate(this.crate);

            userData.addOpenings(1);
            globalData.setLatestOpener(this.player);
            globalData.setDirty(true);

            // --- LOGIC CHANGE HERE ---
            boolean isRerollSystem = this.crate.isRespinEnabled() && this.crate.isRespinRerollMode();

            // If this is standard mode OR Rebuy mode, give rewards immediately.
            // If this is REROLL mode, we WAIT. The GUI will handle giving it.
            if (!isRerollSystem) {
                this.rewards.forEach(reward -> reward.give(this.player));
            }

            // Cooldowns and Milestones (Same as before)
            if (crate.isOpeningCooldownEnabled()) {
                userData.addOpeningStreak(1);
                if (!userData.isOnCooldown() && !crate.hasCooldownBypassPermission(player)) {
                    userData.setCooldown(crate.getOpeningCooldownTime());
                }
            }
            if (crate.hasMilestones()) {
                userData.addMilestones(1);
                plugin.getCrateManager().triggerMilestones(player, crate, userData.getMilestone());
                if (userData.getMilestone() >= crate.getMaxMilestone() && crate.isMilestonesRepeatable()) {
                    userData.setMilestone(0);
                }
            }
            this.plugin.getUserManager().save(user);

            // --- RESPIN / GUI LOGIC ---
            if (this.crate.isRespinEnabled()) {

                // Check how many times they have rerolled already
                int currentRerolls = 0;
                if (player.hasMetadata("excellent_reroll_count")) {
                    currentRerolls = player.getMetadata("excellent_reroll_count").get(0).asInt();
                }

                // If they haven't reached the limit yet...
                if (currentRerolls < this.crate.getRespinLimit()) {
                    this.plugin.getServer().getScheduler().runTaskLater(this.plugin, () -> {
                        if (this.player.isOnline()) {
                            // Pass the rewards to the GUI so it can decide whether to give them or discard them
                            new RespinGUI(this.plugin, this.player, this.crate, this.source, this.rewards).open();
                        } else if (isRerollSystem) {
                            // If player quit before GUI opened, give items to be safe
                            this.rewards.forEach(r -> r.give(this.player));
                        }
                    }, 2L);
                } else {
                    // Limit reached. If we were holding items (Reroll Mode), give them now.
                    if (isRerollSystem) {
                        this.rewards.forEach(reward -> reward.give(this.player));
                        // Clean up metadata
                        player.removeMetadata("excellent_reroll_count", plugin);
                    }
                }
            } else {
                // If Respin NOT enabled, but we somehow skipped giving items? (Shouldn't happen with logic above, but safety check)
                if (isRerollSystem) this.rewards.forEach(reward -> reward.give(this.player));
            }
        }
    }

    @Override
    @NotNull
    public List<Reward> getRewards() {
        return this.rewards;
    }

    @Override
    public void addReward(@NotNull Reward reward) {
        this.rewards.add(reward);
    }

    @Override
    public void addRewards(@NotNull Collection<Reward> rewards) {
        this.rewards.addAll(rewards);
    }

    @Override
    public boolean isRefundable() {
        return this.refundable;
    }

    @Override
    public void setRefundable(boolean refundable) {
        this.refundable = refundable;
    }

    @Override
    @NotNull
    public Player getPlayer() {
        return this.player;
    }

    @Override
    @NotNull
    public CrateSource getSource() {
        return this.source;
    }

    @Override
    @NotNull
    public Crate getCrate() {
        return this.crate;
    }

    @Override
    @Nullable
    public Cost getCost() {
        return this.cost;
    }
}
