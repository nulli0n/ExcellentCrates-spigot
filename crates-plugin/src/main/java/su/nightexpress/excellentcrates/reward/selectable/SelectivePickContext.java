package su.nightexpress.excellentcrates.reward.selectable;

import java.util.List;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.excellentcrates.api.reward.registry.RewardReference;

@NullMarked
public record SelectivePickContext(int requiredRewards, List<RewardReference> selectedRewards) {

}
