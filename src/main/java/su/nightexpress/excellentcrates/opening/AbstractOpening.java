package su.nightexpress.excellentcrates.opening;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.opening.Opening;
import su.nightexpress.excellentcrates.api.opening.Spinner;
import su.nightexpress.excellentcrates.crate.impl.Crate;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.data.impl.CrateUser;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.spinner.AnimationSpinner;
import su.nightexpress.excellentcrates.opening.spinner.RewardSpinner;
import su.nightexpress.nightcore.util.Players;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractOpening extends Runnable implements Opening {

    protected final CratesPlugin         plugin;
    protected final Map<String, Spinner> spinnerMap;
    protected final Player               player;
    protected final CrateSource          source;
    protected final Crate                crate;
    protected final CrateKey             key;

    protected boolean refundable;
    protected boolean hasRewardAttempts;
    protected boolean saveData;

    public AbstractOpening(@NotNull CratesPlugin plugin, @NotNull Player player,
                           @NotNull CrateSource source, @Nullable CrateKey key) {
        this.plugin = plugin;
        this.player = player;
        this.source = source;
        this.crate = source.getCrate();
        this.key = key;
        this.spinnerMap = new LinkedHashMap<>();
        this.setRefundable(true);
    }

    @Nullable
    public Spinner getSpinner(@NotNull String id) {
        return this.spinnerMap.get(id.toLowerCase());
    }

    public void addSpinner(@NotNull Spinner spinner, @NotNull String name/*, int delay*/) {
        this.spinnerMap.put(name.toLowerCase(), spinner);
        //spinner.setStartDelay(delay);
        spinner.run();
    }

    public void stopAnimation(@NotNull String name) {
        Spinner spinner = this.getSpinner(name);
        if (!(spinner instanceof AnimationSpinner)) return;

        this.stopSpinner(spinner);
    }

    public void stopReward(@NotNull String name) {
        Spinner spinner = this.getSpinner(name);
        if (!(spinner instanceof RewardSpinner)) return;

        this.stopSpinner(spinner);
    }

    private void stopSpinner(@NotNull Spinner spinner) {
        if (spinner.isCompleted() || !spinner.isRunning()) return;

        spinner.stop();
    }

    @Override
    protected void onTick() {
        if (!this.isValidPlayer()) {
            this.stop();
            return;
        }
        this.getSpinners().forEach(Spinner::tick);
    }

    @Override
    protected final void onStop() {
        if (this.isRefundable()) {
            if ((this.isEmergency() && !this.hasRewardRolled()) || !this.hasRewardAttempts()) {
                if (this.key != null) {
                    this.plugin.getKeyManager().giveKey(this.player, this.key, 1);
                }
                if (this.source.getItem() != null) {
                    Players.addItem(this.player, this.crate.getItem());
                }
                this.crate.getOpenCostMap().forEach((currency, amount) -> {
                    currency.getHandler().give(this.player, amount);
                });
            }
        }

        if (this.isCompleted()) {
            this.crate.setLastOpener(player.getName());
            this.plugin.getCrateManager().setCrateCooldown(player, crate);
            this.plugin.getCrateManager().addOpenings(player, crate, 1);
            this.plugin.getCrateManager().proceedMilestones(player, crate);

            if (this.isSaveData() && !this.isEmergency()) {
                this.plugin.runTaskAsync(task -> {
                    CrateUser user = this.plugin.getUserManager().getUserData(player);
                    this.crate.saveRewardWinDatas();
                    this.plugin.getUserManager().save(user);
                });
            }
        }

        this.getSpinners().forEach(Spinner::stop);
        this.finalizeStop();
    }

    protected void finalizeStop() {
        this.removeOpening();
    }

    public void removeOpening() {
        this.plugin.getOpeningManager().removeOpening(this.getPlayer());
    }

    public boolean isValidPlayer() {
        return this.getPlayer().isOnline();
    }

    public boolean hasRewardRolled() {
        return this.getSpinners().stream().anyMatch(spinner -> spinner instanceof RewardSpinner && spinner.isCompleted());
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
    public boolean hasRewardAttempts() {
        return hasRewardAttempts;
    }

    @Override
    public void setHasRewardAttempts(boolean hasRewardAttempts) {
        this.hasRewardAttempts = hasRewardAttempts;
    }

    @Override
    public boolean isSaveData() {
        return saveData;
    }

    @Override
    public void setSaveData(boolean saveData) {
        this.saveData = saveData;
    }

    @Override
    @NotNull
    public Player getPlayer() {
        return player;
    }

    @Override
    @NotNull
    public CrateSource getSource() {
        return source;
    }

    @Override
    @NotNull
    public Crate getCrate() {
        return crate;
    }

    @Override
    @Nullable
    public CrateKey getKey() {
        return key;
    }

    @Override
    @NotNull
    public Collection<Spinner> getSpinners() {
        return this.spinnerMap.values();
    }
}
