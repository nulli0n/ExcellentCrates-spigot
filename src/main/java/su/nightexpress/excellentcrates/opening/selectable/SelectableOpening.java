package su.nightexpress.excellentcrates.opening.selectable;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.nightexpress.excellentcrates.CratesPlugin;
import su.nightexpress.excellentcrates.api.crate.Reward;
import su.nightexpress.excellentcrates.crate.cost.Cost;
import su.nightexpress.excellentcrates.crate.impl.CrateSource;
import su.nightexpress.excellentcrates.data.crate.UserCrateData;
import su.nightexpress.excellentcrates.opening.AbstractOpening;
import su.nightexpress.excellentcrates.opening.multiselectable.MultiSelectableRules;
import su.nightexpress.excellentcrates.user.CrateUser;
import su.nightexpress.nightcore.util.random.Rnd;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectableOpening extends AbstractOpening {

    private static final String MULTI_SELECTABLE_ID = "multi_selectable";

    protected final SelectableProvider provider;
    protected final SelectableMenu     menu;
    protected final Set<Reward>        selectedRewards;

    protected boolean confirmed;
    protected boolean completed;
    protected int completionOpeningCount;

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
        this.completionOpeningCount = 1;
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @NotNull
    public List<Reward> getCrateRewards() {
        return this.crate.getRewards(this.player);
    }

    public boolean isMultiSelectable() {
        return MULTI_SELECTABLE_ID.equalsIgnoreCase(this.provider.getId());
    }

    public int getRequiredAmount() {
        int rewardCount = this.getCrateRewards().size();
        if (!this.isMultiSelectable()) {
            return Math.min(rewardCount, this.provider.getSelectionAmount());
        }

        int remainingPaidOpenings = this.cost == null ? 0 : this.cost.countMaxOpenings(this.player);
        int cooldownRemaining = this.getCooldownRemaining();
        return MultiSelectableRules.maxSelections(remainingPaidOpenings, rewardCount, cooldownRemaining);
    }

    private int getCooldownRemaining() {
        if (!this.crate.isOpeningCooldownEnabled() || this.crate.hasCooldownBypassPermission(this.player)) {
            return Integer.MAX_VALUE;
        }

        CrateUser user = this.plugin.getUserManager().getOrFetch(this.player);
        UserCrateData data = user.getCrateData(this.crate);
        return Math.max(1, this.crate.getOpeningLimitAmount() - data.queryOpeningStreak());
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

    public boolean isSelectionLimitReached() {
        return this.getSelectedAmount() >= this.getRequiredAmount();
    }

    public boolean canConfirm() {
        if (!this.isMultiSelectable()) {
            return this.isAllRewardsSelected();
        }
        int selected = this.getSelectedAmount();
        return selected > 0 && selected <= this.getRequiredAmount();
    }

    public boolean isAllRewardsSelected() {
        return this.getSelectedAmount() == this.getRequiredAmount();
    }

    public boolean giveSelectedRewards() {
        if (!this.canConfirm()) return false;

        int selectedAmount = this.getSelectedAmount();
        if (this.isMultiSelectable() && !this.takeAdditionalCosts(selectedAmount)) return false;

        this.completionOpeningCount = this.isMultiSelectable() ? MultiSelectableRules.openingCount(selectedAmount) : 1;
        this.setRefundable(false);

        this.addRewards(this.selectedRewards);
        this.selectedRewards.clear();
        this.completed = true;
        return true;
    }

    private boolean takeAdditionalCosts(int selectedAmount) {
        int extraCosts = MultiSelectableRules.extraCosts(selectedAmount);
        if (extraCosts <= 0) return true;
        if (this.cost == null) return false;

        int spent = 0;
        while (spent < extraCosts) {
            if (!this.cost.canAfford(this.player)) {
                while (spent-- > 0) {
                    this.cost.refundAll(this.player);
                }
                return false;
            }

            this.cost.takeAll(this.player);
            spent++;
        }
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
            this.confirmed = this.giveSelectedRewards();
            return;
        }

        if (!this.menu.isViewer(this.player)) {
            this.menu.open(this.player, this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (this.menu.isViewer(this.player)) {
            this.player.closeInventory();
        }
    }

    @Override
    protected void onComplete() {
        if (!this.isMultiSelectable() || this.completionOpeningCount <= 1) return;

        int extraOpenings = this.completionOpeningCount - 1;
        CrateUser user = this.plugin.getUserManager().getOrFetch(this.player);
        UserCrateData userData = user.getCrateData(this.crate);

        userData.addOpenings(extraOpenings);

        if (this.crate.isOpeningCooldownEnabled()) {
            userData.addOpeningStreak(extraOpenings);
        }

        if (this.crate.hasMilestones()) {
            for (int count = 0; count < extraOpenings; count++) {
                userData.addMilestones(1);
                this.plugin.getCrateManager().triggerMilestones(this.player, this.crate, userData.getMilestone());

                if (userData.getMilestone() >= this.crate.getMaxMilestone() && this.crate.isMilestonesRepeatable()) {
                    userData.setMilestone(0);
                }
            }
        }
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public void instaRoll() {
        List<Reward> rewards = this.getCrateRewards();

        if (this.isMultiSelectable()) {
            if (!rewards.isEmpty()) {
                Reward reward = rewards.remove(Rnd.get(rewards.size()));
                this.selectedRewards.add(reward);
            }
        }
        else {
            while (!this.isAllRewardsSelected() && !rewards.isEmpty()) {
                Reward reward = rewards.remove(Rnd.get(rewards.size()));
                this.selectedRewards.add(reward);
            }
        }

        this.giveSelectedRewards();
        this.stop();
    }

    @NotNull
    public SelectableProvider getProvider() {
        return this.provider;
    }
}
