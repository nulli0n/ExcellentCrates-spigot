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

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SelectableOpening extends AbstractOpening {

    protected final SelectableProvider provider;
    protected final SelectableMenu menu;
    protected final Set<Reward> selectedRewards;

    protected boolean confirmed;
    protected boolean completed;

    private int[] displaySlots;
    private List<Reward> shuffledRewards;

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
    public long getInterval() { return 1L; }

    @NotNull
    public List<Reward> getRewards() { return this.crate.getRewards(this.player); }

    public int getRequiredAmount() { return Math.min(this.getRewards().size(), this.provider.getSelectionAmount()); }

    public int getSelectedAmount() { return this.selectedRewards.size(); }

    @NotNull
    public Set<Reward> getSelectedRewards() { return this.selectedRewards; }

    public void addSelectedReward(@NotNull Reward reward) { this.selectedRewards.add(reward); }

    public void removeSelectedReward(@NotNull Reward reward) { this.selectedRewards.remove(reward); }

    public boolean isSelectedReward(@NotNull Reward reward) { return this.selectedRewards.contains(reward); }

    public boolean isAllRewardsSelected() { return this.getSelectedAmount() == this.getRequiredAmount(); }

    public boolean giveSelectedRewards() {
        if (!this.isAllRewardsSelected()) return false;
        this.setRefundable(false);
        this.selectedRewards.forEach(reward -> reward.give(this.player));
        this.selectedRewards.clear();
        this.completed = true;
        return true;
    }

    public void confirm() { this.confirmed = true; }

    @Override
    protected void onStart() {}

    @Override
    protected void onTick() {
        if (this.confirmed) {
            this.confirmed = this.giveSelectedRewards();
            return;
        }
        if (!this.menu.isViewer(this.player)) this.menu.open(this.player, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.menu.isViewer(this.player)) this.player.closeInventory();
    }

    @Override
    protected void onComplete() {}

    @Override
    public boolean isCompleted() { return this.completed; }

    @Override
    public void instaRoll() {
        List<Reward> rewards = this.getRewards();
        while (!this.isAllRewardsSelected() && !rewards.isEmpty()) {
            Reward reward = rewards.remove(Rnd.get(rewards.size()));
            this.selectedRewards.add(reward);
        }
        this.giveSelectedRewards();
        this.stop();
    }

    @NotNull
    public SelectableProvider getProvider() { return this.provider; }

    private static List<int[]> rowRuns(int[] base) {
        List<int[]> runs = new ArrayList<>();
        int start = 0;
        for (int i = 1; i <= base.length; i++) {
            boolean split = (i == base.length);
            if (!split) {
                int prev = base[i - 1], cur = base[i];
                if (cur != prev + 1 || (cur / 9) != (prev / 9)) split = true;
            }
            if (split) {
                runs.add(Arrays.copyOfRange(base, start, i));
                start = i;
            }
        }
        return runs;
    }

    public int[] getDisplaySlots(int[] base, boolean randomize, int visibleCount) {
        if (!randomize) return base;
        if (this.displaySlots == null) {
            List<int[]> runs = rowRuns(base);
            int[] run = runs.isEmpty() ? base : runs.get(0); // first row segment
            int n = Math.min(visibleCount, run.length);

            int[] block = Arrays.copyOfRange(run, 0, n); // start at first reward slot
            Set<Integer> used = new HashSet<>();
            for (int s : block) used.add(s);

            int[] tail = java.util.stream.IntStream.of(base).filter(s -> !used.contains(s)).toArray();

            this.displaySlots = new int[base.length];
            System.arraycopy(block, 0, this.displaySlots, 0, block.length);
            System.arraycopy(tail, 0, this.displaySlots, block.length, tail.length);
        }
        return this.displaySlots;
    }

    public List<Reward> getDisplayRewards(boolean randomize) {
        if (!randomize) return this.getRewards();
        if (this.shuffledRewards == null) {
            this.shuffledRewards = new ArrayList<>(this.getRewards());
            Collections.shuffle(this.shuffledRewards);
        }
        return this.shuffledRewards;
    }
}
