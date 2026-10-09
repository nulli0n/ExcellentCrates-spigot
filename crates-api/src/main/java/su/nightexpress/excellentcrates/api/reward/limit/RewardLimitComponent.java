package su.nightexpress.excellentcrates.api.reward.limit;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.common.limit.LimitOptions;
import su.nightexpress.excellentcrates.api.common.limit.LimitType;
import su.nightexpress.excellentcrates.api.reward.component.RewardComponent;
import su.nightexpress.excellentcrates.api.reward.registry.RewardId;

@NullMarked
public interface RewardLimitComponent extends RewardComponent {

    boolean isEnabled();

    void setEnabled(boolean enabled);

    LimitOptions getOptions(LimitType type);

    void setOptions(LimitType type, LimitOptions options);

    LimitOptions getGlobalOptions();

    void setGlobalOptions(LimitOptions options);

    LimitOptions getIndividualOptions();

    void setIndividualOptions(LimitOptions options);

    boolean isAlternativeEnabled();

    void setAlternativeEnabled(boolean enabled);

    RewardId getAlternativeRewardId();

    void setAlternativeRewardId(RewardId rewardId);
}
