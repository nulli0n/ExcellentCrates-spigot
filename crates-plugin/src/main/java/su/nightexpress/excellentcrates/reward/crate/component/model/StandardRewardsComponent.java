package su.nightexpress.excellentcrates.reward.crate.component.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardEntry;
import su.nightexpress.excellentcrates.api.reward.crate.CrateRewardsComponent;

@NullMarked
public class StandardRewardsComponent implements CrateRewardsComponent {

    private final Map<Identifier, CrateRewardEntry> rewardByIdMap;

    private int requiredRewards;

    public StandardRewardsComponent(Map<Identifier, CrateRewardEntry> rewardByIdMap, int requiredRewards) {
        this.rewardByIdMap = new HashMap<>(rewardByIdMap);
        this.requiredRewards = requiredRewards;
    }

    public static StandardRewardsComponent createDefault() {
        return new StandardRewardsComponent(Map.of(), 1);
    }

    @Override
    public void removeReward(Identifier rewardId) {
        this.rewardByIdMap.remove(rewardId);
    }

    @Override
    public void addReward(CrateRewardEntry reward) {
        this.rewardByIdMap.put(reward.getRewardId(), reward);
    }

    @Override
    public boolean hasReward(Identifier rewardId) {
        return this.rewardByIdMap.containsKey(rewardId);
    }

    @Override
    public @Nullable CrateRewardEntry getReward(Identifier id) {
        return this.rewardByIdMap.get(id);
    }

    @Override
    public Map<Identifier, CrateRewardEntry> getRewardByIdMap() {
        return Collections.unmodifiableMap(this.rewardByIdMap);
    }

    @Override
    public Collection<CrateRewardEntry> getRewards() {
        return Collections.unmodifiableCollection(this.rewardByIdMap.values());
    }

    @Override
    public int getRollCount() {
        return this.requiredRewards;
    }

    @Override
    public void setRollCount(int requiredRewards) {
        this.requiredRewards = requiredRewards;
    }
}
