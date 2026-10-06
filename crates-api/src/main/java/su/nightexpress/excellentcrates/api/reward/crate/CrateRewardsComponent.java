package su.nightexpress.excellentcrates.api.reward.crate;

import java.util.Collection;
import java.util.Map;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import su.nightexpress.engine.id.Identifier;
import su.nightexpress.excellentcrates.api.crate.component.CrateComponent;

@NullMarked
public interface CrateRewardsComponent extends CrateComponent {

    void removeReward(Identifier rewardId);

    void addReward(CrateRewardEntry reward);

    boolean hasReward(Identifier rewardId);

    @Nullable
    CrateRewardEntry getReward(Identifier id);

    Map<Identifier, CrateRewardEntry> getRewardByIdMap();

    Collection<CrateRewardEntry> getRewards();

    int getRequiredAmount();

    void setRequiredAmount(int requiredAmount);
}
