package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.data.crate.GlobalCrateData;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.nightcore.util.Players;

public abstract class AbstractOpening implements Opening {

    protected final CratesPlugin plugin;
    protected final Player       player;
    protected final CrateSource  source;
    protected final Crate        crate;
    protected final CrateKey     key;

    protected long    tickCount;
    protected boolean running;
    protected boolean refundable;

    public AbstractOpening(@NotNull CratesPlugin plugin, @NotNull Player player, @NotNull CrateSource source, @Nullable CrateKey key) {
        this.plugin = plugin;
        this.player = player;
        this.source = source;
        this.crate = source.getCrate();
        this.key = key;
        this.setRefundable(true);
    }

    @Override
    public void run() {
        if (this.isRunning()) return;

        this.running = true;
        this.onStart();
    }

    @Override
    public void stop() {
        if (!this.isRunning()) return;

        this.running = false;
        this.onStop();
    }

    @Override
    public void tick() {
        if (!this.isRunning()) return;

        if (this.isCompleted()) {
            this.stop();
            return;
        }

        if (this.isTickTime()) {
            this.onTick();
            //this.tickCount = 0L;
        }

        this.tickCount = Math.max(0L, this.tickCount + 1L);
    }

    @Override
    public boolean isRunning() {
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
        if (this.isRefundable()) {
            if (this.key != null) {
                this.plugin.getKeyManager().giveKey(this.player, this.key, 1);
            }
            if (this.source.getItem() != null) {
                Players.addItem(this.player, this.crate.getItem());
            }

            this.crate.refundForOpen(this.player);
        }

        if (this.isCompleted()) {
            this.onComplete();

            CrateUser user = plugin.getUserManager().getOrFetch(player);
            UserCrateData userData = user.getCrateData(this.crate);
            GlobalCrateData globalData = plugin.getDataManager().getCrateDataOrCreate(this.crate);

            userData.addOpenings(1);
            globalData.setLatestOpener(this.player);
            globalData.setSaveRequired(true);

            if (crate.hasOpenCooldown() && !crate.hasCooldownBypassPermission(player)) {
                userData.setCooldown(crate.getOpenCooldown());
            }

            if (crate.hasMilestones()) {
                userData.addMilestones(1);
                plugin.getCrateManager().triggerMilestones(player, crate, userData.getMilestone());
                if (userData.getMilestone() >= crate.getMaxMilestone() && crate.isMilestonesRepeatable()) {
                    userData.setMilestone(0);
                }
            }

            this.plugin.getUserManager().save(user);
        }

        this.plugin.getOpeningManager().removeOpening(this.getPlayer());
    }

    @Override
    public boolean isRefundable() {
        return refundable;
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
    public CrateKey getKey() {
        return this.key;
    }
}
