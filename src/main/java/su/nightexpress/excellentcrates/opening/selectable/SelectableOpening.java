package su.nightexpress.excellentcrates.opening.selectable;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.key.CrateKey;
import su.nightexpress.excellentcrates.opening.AbstractOpening;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectableOpening extends AbstractOpening {

    protected final SelectableProvider provider;
    protected final SelectableMenu     menu;
    protected final Set<Reward>  selectedRewards;

    private final java.util.Set<String> overrideDisabled = new java.util.HashSet<>();
    private final java.util.Map<String, Integer> redoLeft = new java.util.HashMap<>();

    protected boolean confirmed;
    protected boolean completed;

    private List<Reward> shuffledRewards;

    private String keyOf(@NotNull Reward reward) {
        return reward.getId();
    }

    public SelectableOpening(@NotNull CratesPlugin plugin,
                            @NotNull SelectableProvider provider,
                            @NotNull SelectableMenu menu,
                            @NotNull Player player,
                            @NotNull CrateSource source,
                            @Nullable CrateKey key) {
        super(plugin, player, source, key);
        this.menu = menu;
        this.provider = provider;
        this.selectedRewards = new HashSet<>();
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @NotNull
    public List<Reward> getRewards() {
        return this.crate.getRewards(this.player);
    }

    public int getRequiredAmount() {
        return Math.min(this.getRewards().size(), this.provider.getSelectionAmount());
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
        this.onSelected(reward);
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

        this.selectedRewards.forEach(reward -> reward.give(this.player));
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
        List<Reward> rewards = this.getRewards();
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

    @NotNull
    public List<Reward> getDisplayRewards(boolean randomizeSlots) {
        if (!randomizeSlots) {
            return this.getRewards();
        }
        if (this.shuffledRewards == null) {
            this.shuffledRewards = new ArrayList<>(this.getRewards());
            Collections.shuffle(this.shuffledRewards);
        }
        return this.shuffledRewards;
    }

    // General Logic for locked rewards & materialoverride, gives a "scratchoff" experience.
    public boolean isOverrideDisabled(@NotNull Reward reward) {
        return this.overrideDisabled.contains(keyOf(reward));
    }

    public int getRedoLeft(@NotNull Reward reward) {
        if (this.redoLeft.isEmpty()) return this.provider.getLockSelectionRedos();
        int min = Integer.MAX_VALUE;
        for (Integer v : this.redoLeft.values()) {
            if (v != null && v < min) min = v;
        }
        return min == Integer.MAX_VALUE ? this.provider.getLockSelectionRedos() : min;
    }

    public void onSelected(@NotNull Reward reward) {
        if (this.provider.isLockSelection()) {
            String k = keyOf(reward);
            this.overrideDisabled.add(k);
            if (!this.redoLeft.containsKey(k)) {
                int shared = this.getRedoLeft(reward);
                this.redoLeft.put(k, shared);
            }
        }
    }

    public boolean tryUnselect(@NotNull Reward reward) {
        if (!this.isSelectedReward(reward)) return false;
        if (!this.provider.isLockSelection()) {
            this.removeSelectedReward(reward);
            return true;
        }
        int left = this.getRedoLeft(reward);
        if (left <= 0) return false;
        for (java.util.Map.Entry<String,Integer> e : this.redoLeft.entrySet()) {
            int cur = e.getValue() == null ? this.provider.getLockSelectionRedos() : e.getValue();
            e.setValue(Math.max(0, cur - 1));
        }
        this.removeSelectedReward(reward);
        return true;
    }

}
