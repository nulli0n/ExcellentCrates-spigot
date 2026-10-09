package su.nightexpress.excellentcrates.api.reward.registry;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.engine.id.Identifier;

@NullMarked
public record RewardId(Identifier crateId, Identifier rewardId) {

}
