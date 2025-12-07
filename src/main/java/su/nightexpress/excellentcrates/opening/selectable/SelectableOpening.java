package su.nightexpress.excellentcrates.opening.selectable;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.opening.AbstractOpening;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectableOpening extends AbstractOpening {

    protected final SelectableProvider provider;
    protected final SelectableMenu     menu;
    protected final Set<Reward>  selectedRewards;

    protected boolean confirmed;
    protected boolean completed;

    public SelectableOpening(@NotNull CratesPlugin plugin,
                            @NotNull SelectableProvider provider,
                            @NotNull SelectableMenu menu,
                            @NotNull Player player,
                            @NotNull CrateSource source,
                            @Nullable Cost cost) {
        super(plugin, player, source, cost);
        this.menu = menu;
        this.provider = provider;
        this.selectedRewards = new HashSet<>();
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @NotNull
    public List<Reward> getCrateRewards() {
        return this.crate.getRewards(this.player);
    }

    public int getRequiredAmount() {
        return Math.min(this.getCrateRewards().size(), this.provider.getSelectionAmount());
    }

    public int getSelectedAmount() {
        return this.selectedRewards.size();
    }

    @NotNull
    public Set<Reward> getSelectedRewards() {
        return this.selectedRewards;
    }

    public void addSelectedReward(@NotNull Reward reward) {
        this.selectedRewards.add(reward);
    }

    public void removeSelectedReward(@NotNull Reward reward) {
        this.selectedRewards.remove(reward);
    }

    public boolean isSelectedReward(@NotNull Reward reward) {
        return this.selectedRewards.contains(reward);
    }

    public boolean isAllRewardsSelected() {
        return this.getSelectedAmount() == this.getRequiredAmount();
    }

    public boolean giveSelectedRewards() {
        if (!this.isAllRewardsSelected()) return false;

        this.setRefundable(false);

        this.addRewards(this.selectedRewards);
        this.selectedRewards.clear();
        this.completed = true; // Use explicit variable to ensure all checks and validations are passed.
        return true;
    }

    public void confirm() {
        this.confirmed = true;
    }

    @Override
    protected void onStart() {

    }

    @Override
    protected void onTick() {
        if (this.confirmed) {
            this.confirmed = this.giveSelectedRewards(); // Try give rewards next tick, cancel confirmation if could not.
            return;
        }

        // Open menu next tick, not on start, because we don't need it in case of (mass-)instant openings.
        if (!this.menu.isViewer(this.player)) {
            this.menu.open(this.player, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.menu.isViewer(this.player)) {
            this.player.closeInventory(); // Let the GUI handle close event properly.
        }
    }

    @Override
    protected void onComplete() {

    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public void instaRoll() {
        // Just give random rewards I assume?
        List<Reward> rewards = this.getCrateRewards();
        while (!this.isAllRewardsSelected() && !rewards.isEmpty()) {
            Reward reward = rewards.remove(Rnd.get(rewards.size()));
            this.selectedRewards.add(reward);
        }

        this.giveSelectedRewards();
        this.stop();
    }

    @NotNull
    public SelectableProvider getProvider() {
        return this.provider;
    }
}
