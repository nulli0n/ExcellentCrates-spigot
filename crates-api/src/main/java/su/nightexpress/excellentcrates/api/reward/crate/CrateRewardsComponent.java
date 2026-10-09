package su.nightexpress.excellentcrates.api.reward.crate;

import java.util.Collection;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface CrateRewardsComponent extends CrateComponent {

    boolean hasReward(Identifier rewardId);

    void addReward(CrateRewardEntry reward);

    void removeReward(Identifier rewardId);

    @Nullable
    CrateRewardEntry getReward(Identifier id);

    Map<Identifier, CrateRewardEntry> getRewardByIdMap();

    Collection<CrateRewardEntry> getRewards();

    int getRollCount();

    void setRollCount(int rollCount);
}
